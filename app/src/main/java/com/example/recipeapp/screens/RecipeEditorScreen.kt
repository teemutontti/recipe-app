package com.example.recipeapp.screens

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.ApplicationContext
import com.example.recipeapp.R
import com.example.recipeapp.composables.BackButton
import com.example.recipeapp.composables.IngredientForm
import com.example.recipeapp.composables.IngredientRow
import com.example.recipeapp.composables.InstructionRow
import com.example.recipeapp.composables.NumberCounter
import com.example.recipeapp.composables.RecipeImage
import com.example.recipeapp.composables.StepIndicator
import com.example.recipeapp.models.Ingredient
import com.example.recipeapp.models.Instruction
import com.example.recipeapp.utils.Utils
import com.example.recipeapp.viewmodels.PersonalRecipesViewModel
import com.example.recipeapp.viewmodels.RecipeUnderInspectionViewModel
import com.example.recipeapp.viewmodels.ViewModelWrapper

@Composable
fun RecipeEditorScreen(navController: NavController, viewModels: ViewModelWrapper) {
    var currentFormStep by remember { mutableIntStateOf(0) }
    var allowNext by remember { mutableStateOf(false) }

    fun handleSave() {
        val recipe = viewModels.inspection.recipe.value
        Log.d("RecipeEditor", "Id: ${recipe.id}")
        viewModels.personal.isRecipeInDatabase(recipe) { isFound ->
            if (isFound) {
                if (recipe.id == -1) {
                    Log.d("Editor", "Addition save 1: $recipe")
                    viewModels.personal.add(recipe)
                }
                Log.d("Editor", "Editing save: $recipe")
                viewModels.personal.edit(recipe)
            } else {
                Log.d("Editor", "Addition save 2: $recipe")
                viewModels.personal.add(recipe)
            }
            navController.navigate("cookbook")
        }
    }

    fun handleStepChange(direction: Int) {
        val newCurrentFormStep = currentFormStep + direction
        if (newCurrentFormStep in 0..4) {
            currentFormStep = newCurrentFormStep
            if (newCurrentFormStep == 4) {
                handleSave()
            }
        }
        allowNext = direction < 0
    }

    fun handleAllowNextChange(newValue: Boolean) {
        allowNext = newValue
    }

    Scaffold(
        topBar = { BackButton(navController) },
        content = {
            RecipeEditorContent(
                navController = navController,
                paddingValues = it,
                viewModels = viewModels,
                currentFormStep = currentFormStep,
                handleAllowNextChange = ::handleAllowNextChange,
                handleStepChange = ::handleStepChange,
            )
        },
        bottomBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(24.dp)
            ) {
                if (currentFormStep > 0) {
                    Button(
                        modifier = Modifier.weight(1f).height(44.dp),
                        onClick = { handleStepChange(-1) },
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.surface),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                    ) {
                        Text("Previous")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Button(
                    enabled = allowNext,
                    modifier = Modifier.weight(1f).height(44.dp),
                    onClick = { handleStepChange(1) }
                ) {
                    Text(if (currentFormStep == 3) "Save" else "Next")
                }
            }
        }
    )
}

@Composable
private fun RecipeEditorContent(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModels: ViewModelWrapper,
    currentFormStep: Int,
    handleAllowNextChange: (Boolean) -> Unit,
    handleStepChange: (Int) -> Unit,
) {
    val viewModel: RecipeUnderInspectionViewModel = viewModels.inspection

    Column(modifier = Modifier
        .padding(paddingValues)
        .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.padding(horizontal = 40.dp)) {
            when (currentFormStep) {
                0 -> Text("General", style = MaterialTheme.typography.headlineLarge)
                1 -> Text("Ingredients", style = MaterialTheme.typography.headlineLarge)
                2 -> Text("Instructions", style = MaterialTheme.typography.headlineLarge)
                3 -> Text("Preview", style = MaterialTheme.typography.headlineLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))
            StepIndicator(0..3, currentFormStep)
            Spacer(modifier = Modifier.height(32.dp))
            when (currentFormStep) {
                0 -> TitleStep(viewModel, handleAllowNextChange, handleStepChange)
                1 -> IngredientsStep(viewModel, handleAllowNextChange)
                2 -> InstructionsStep(viewModel, handleAllowNextChange)
                3 -> PreviewStep(viewModels, handleAllowNextChange, navController)
            }
        }
    }
}

@Composable
private fun TitleStep(
    viewModel: RecipeUnderInspectionViewModel,
    handleAllowNextChange: (Boolean) -> Unit,
    toNextStep: (Int) -> Unit
) {
    var title: String by remember { mutableStateOf(viewModel.recipe.value.title) }
    var servings by remember { mutableIntStateOf(viewModel.recipe.value.servings) }
    var image by remember { mutableStateOf(viewModel.recipe.value.image) }

    LaunchedEffect(title, servings) {
        val titleOk = Utils.Validator.recipeTitle(title)
        val servingsOk = Utils.Validator.recipeServings(servings)

        if (titleOk && servingsOk) {
            val newRecipe = viewModel.recipe.value.copy(
                title = title,
                servings = servings
            )
            viewModel.setRecipe(newRecipe)
            handleAllowNextChange(true)
        } else {
            handleAllowNextChange(false)
        }
    }

    val context = ApplicationContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                // Persist access to the uri even after app restarts
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                // Saving image uri string
                image = it.toString()
            }
       },
    )

    fun handlePickImage() {
        launcher.launch("image/*")
    }
    
    fun handleTakePhoto() {
        // TODO: Add functionality
    }

    /* === TITLE SECTION === */
    TextField(
        value = title,
        onValueChange = { title = it },
        placeholder = { Text("Insert title...") },
        label = { Text("Title *") },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { toNextStep(1) }
        )
    )
    Spacer(modifier = Modifier.height(24.dp))

    /* === SERVING SIZE SECTION === */
    Text("Set serving size:", style = MaterialTheme.typography.headlineSmall)
    Spacer(modifier = Modifier.height(8.dp))
    NumberCounter(
        value = servings,
        onNumberChange = { servings = it },
        max = 40
    )
    Spacer(modifier = Modifier.height(24.dp))

    /* === IMAGE SECTION === */
    // TODO: Add take photo functionality
    Text("Change image:", style = MaterialTheme.typography.headlineSmall)
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedButton(
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(0.dp),
        onClick = { handlePickImage() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.height(Utils.IMAGE_HEIGHT.dp)
        ) {
            if (image == "") {
                RecipeImage(painter = painterResource(id = R.drawable.meal))
            } else {
                RecipeImage(model = image)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.7f),
                                Color.Black.copy(alpha = 0.3f),
                                Color.Black.copy(alpha = 0.7f),
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(0f, Float.POSITIVE_INFINITY)
                        )
                    )
            )
            Icon(Icons.Rounded.Image, "image")
        }
    }
}

@Composable
private fun IngredientsStep(
    viewModel: RecipeUnderInspectionViewModel,
    handleAllowNextChange: (Boolean) -> Unit
) {
    LaunchedEffect(viewModel.recipe.value) {
        if (viewModel.recipe.value.ingredients.isEmpty()) {
            handleAllowNextChange(false)
        } else {
            handleAllowNextChange(true)
        }
    }

    fun handleIngredientAdd(ingredient: Ingredient) {
        // TODO: Clear data on save
        viewModel.addIngredient(ingredient)
    }

    fun handleIngredientDelete(index: Int) {
        viewModel.deleteIngredient(index)
    }

    IngredientForm { handleIngredientAdd(it) }

    Spacer(modifier = Modifier.height(16.dp))

    if (viewModel.recipe.value.ingredients.isNotEmpty()) {
        Text("Current ingredients:")
        Spacer(modifier = Modifier.height(8.dp))
        viewModel.recipe.value.ingredients.mapIndexed { index, ingredient ->
            IngredientRow(index, ingredient, handleDelete = { handleIngredientDelete(index) })
        }
    }
}

@Composable
private fun InstructionsStep(
    viewModel: RecipeUnderInspectionViewModel,
    handleAllowNextChange: (Boolean) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val instructions = remember { mutableStateListOf<Instruction>() }

    LaunchedEffect(Unit) {
        instructions.addAll(viewModel.recipe.value.instructions)
    }

    LaunchedEffect(instructions.size) {
        if (instructions.isNotEmpty()) {
            handleAllowNextChange(true)
        } else {
            handleAllowNextChange(false)
        }
    }

    fun addInstruction() {
        if (text.isNotEmpty()) {
            // Cleaning up line breaks
            text = text.replace("\n", "")

            // Generating the next steps number
            val nextNumber = if (instructions.isEmpty()) 1 else instructions.size + 1

            // Generating the new instruction
            val newInstruction = Instruction(nextNumber, text)

            // Adding to viewModel state
            viewModel.addInstruction(newInstruction)

            // Setting state in composable
            instructions.clear()
            instructions.addAll(viewModel.recipe.value.instructions)

            // Setting the text-field text state to null
            text = ""
        }
    }

    fun deleteInstruction(index: Int) {
        instructions.removeAt(index)

        // Mapping instructions to set new instruction numbers
        val newInstructions = instructions.mapIndexed { i, instruction ->
            instruction.copy(number = i + 1)
        }

        instructions.clear()
        instructions.addAll(newInstructions)

        // Overwriting the whole recipe so instruction numbers are also updated to view model
        val newRecipe = viewModel.recipe.value.copy(instructions = newInstructions)
        viewModel.setRecipe(newRecipe)
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = text,
            shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp, bottomEnd = 4.dp),
            onValueChange = { text = it },
            label = { Text("Text") },
            modifier = Modifier.weight(0.9f).padding(0.dp)
        )
        Button(
            onClick = ::addInstruction,
            shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            modifier = Modifier.fillMaxHeight(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        ) {
            Icon(Icons.Rounded.Add, "add")
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    if (instructions.isNotEmpty()) {
        Text("Current instructions")
        Spacer(modifier = Modifier.height(8.dp))
        instructions.forEachIndexed { index, instruction ->
            InstructionRow(index, instruction, ::deleteInstruction)
        }
    }
}

@Composable
private fun PreviewStep(
    viewModels: ViewModelWrapper,
    handleAllowNextChange: (Boolean) -> Unit,
    navController: NavController
) {
    var showPreview by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        handleAllowNextChange(true)
    }

    Column {
        Text(
            text = "Title:",
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray.copy(alpha = 0.8f)
            )
        )
        Text(viewModels.inspection.recipe.value.title)
    }
    Spacer(modifier = Modifier.height(8.dp))
    Column {
        Text(
            text = "Image:",
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray.copy(alpha = 0.8f)
            )
        )
        Text(viewModels.inspection.recipe.value.image)
    }
    Spacer(modifier = Modifier.height(8.dp))
    Column {
        Text(
            text = "Servings:",
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray.copy(alpha = 0.8f)
            )
        )
        Text("${viewModels.inspection.recipe.value.servings}")
    }
    Spacer(modifier = Modifier.height(8.dp))
    Column {
        Text(
            text = "Ingredients:",
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray.copy(alpha = 0.8f)
            )
        )
        Column {
            viewModels.inspection.recipe.value.ingredients.forEach {
                Text("${it.amount} ${it.unit}  ${it.name}")
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    Column {
        Text(
            text = "Instructions:",
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray.copy(alpha = 0.8f)
            )
        )
        Column {
            viewModels.inspection.recipe.value.instructions.forEach { instruction ->
                Row {
                    Text("${instruction.number}")
                    Text(instruction.text)
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    TextButton(
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(vertical = 0.dp, horizontal = 8.dp),
        onClick = { showPreview = !showPreview }
    ) {
        Text("Toggle Show Preview")
    }
    Spacer(modifier = Modifier.height(16.dp))
    if (showPreview) {
        Column(
            Modifier
                .fillMaxSize()
                .shadow(4.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
        ) {
            RecipeScreen(navController, viewModels, preview = true)
        }
    }
}