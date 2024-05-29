package com.example.recipeapp.screens

import android.content.Intent
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.ApplicationContext
import com.example.recipeapp.R
import com.example.recipeapp.composables.IngredientForm
import com.example.recipeapp.composables.IngredientRow
import com.example.recipeapp.composables.InstructionRow
import com.example.recipeapp.composables.NumberCounter
import com.example.recipeapp.composables.RecipeImage
import com.example.recipeapp.composables.StepIndicator
import com.example.recipeapp.models.room.PersonalIngredient
import com.example.recipeapp.models.room.PersonalInstruction
import com.example.recipeapp.utils.Utils
import com.example.recipeapp.viewmodels.PersonalRecipesViewModel
import com.example.recipeapp.viewmodels.RecipeUnderInspectionViewModel

@Composable
fun RecipeEditorScreen(navController: NavController) {
    val personalRecipesViewModel: PersonalRecipesViewModel = viewModel()
    val recipeUnderInspectionViewModel: RecipeUnderInspectionViewModel = viewModel()

    var currentFormStep by remember { mutableIntStateOf(0) }
    var allowNext by remember { mutableStateOf(false) }

    fun onSave() {
        val recipe = recipeUnderInspectionViewModel.recipe
        if (recipe.id == -1) {
            personalRecipesViewModel.add(recipe)
        } else {
            personalRecipesViewModel.edit(recipe)
        }
        navController.navigate("cookbook")
    }

    fun handleStepChange(direction: Int) {
        val newCurrentFormStep = currentFormStep + direction
        if (newCurrentFormStep in 0..4) {
            currentFormStep = newCurrentFormStep
            if (newCurrentFormStep == 4) {
                onSave()
            }
        }
        allowNext = direction < 0
    }

    fun handleAllowNextChange(newValue: Boolean) {
        allowNext = newValue
    }

    Scaffold(
        topBar = {
            TextButton(onClick = { navController.navigateUp() }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "back",
                        modifier = Modifier.height(16.dp).width(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Back")
                }
            }
        },
        content = {
            RecipeEditorContent(
                navController = navController,
                paddingValues = it,
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
    currentFormStep: Int,
    handleAllowNextChange: (Boolean) -> Unit,
    handleStepChange: (Int) -> Unit,
) {
    val viewModel: RecipeUnderInspectionViewModel = viewModel()

    Column(modifier = Modifier
        .padding(paddingValues)
        .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                StepIndicator(
                    steps = 0..3,
                    currentStep = currentFormStep,
                )
                Spacer(modifier = Modifier.height(32.dp))
                when (currentFormStep) {
                    0 -> TitleStep(viewModel, handleAllowNextChange, handleStepChange)
                    1 -> IngredientsStep(viewModel, handleAllowNextChange)
                    2 -> InstructionsStep(viewModel, handleAllowNextChange)
                    3 -> PreviewStep(viewModel, handleAllowNextChange, navController)
                }
            }
        }
    }
}

@Composable
private fun TitleStep(
    recipeUnderInspectionViewModel: RecipeUnderInspectionViewModel,
    handleAllowNextChange: (Boolean) -> Unit,
    toNextStep: (Int) -> Unit
) {
    var title: String by remember {
        mutableStateOf(recipeUnderInspectionViewModel.recipe.title)
    }
    var servings by remember {
        mutableIntStateOf(recipeUnderInspectionViewModel.recipe.servings)
    }
    var image by remember {
        mutableStateOf(recipeUnderInspectionViewModel.recipe.image)
    }

    LaunchedEffect(title, servings) {
        val titleOk = Utils.Validator.recipeTitle(title)
        val servingsOk = Utils.Validator.recipeServings(servings)

        if (titleOk && servingsOk) {
            val newRecipe = recipeUnderInspectionViewModel.recipe.copy(
                title = title,
                servings = servings
            )
            recipeUnderInspectionViewModel.setRecipe(newRecipe)
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

    Text(
        text = "Title & Image",
        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp)
    )
    Spacer(modifier = Modifier.height(16.dp))
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
    Text("Set serving size:", style = TextStyle(fontWeight = FontWeight.SemiBold))
    Spacer(modifier = Modifier.height(8.dp))
    NumberCounter(
        value = servings,
        onNumberChange = { servings = it },
        max = 40
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text("Change image:", style = TextStyle(fontWeight = FontWeight.SemiBold))
    Spacer(modifier = Modifier.height(8.dp))
    /* TODO: Add take a photo functionality
    Button(
        contentPadding = PaddingValues(horizontal = 8.dp),
        onClick = { handleTakePhoto() }
    ) {
        Icon(Icons.Rounded.CameraAlt, "camera")
        Text("Take a Photo")
    }
     */
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
    recipeUnderInspectionViewModel: RecipeUnderInspectionViewModel,
    handleAllowNextChange: (Boolean) -> Unit
) {
    LaunchedEffect(recipeUnderInspectionViewModel.recipe) {
        if (recipeUnderInspectionViewModel.recipe.ingredients.isEmpty()) {
            handleAllowNextChange(false)
        } else {
            handleAllowNextChange(true)
        }
    }

    fun handleIngredientAdd(ingredient: PersonalIngredient) {
        recipeUnderInspectionViewModel.addIngredient(ingredient)
    }

    fun handleIngredientDelete(index: Int) {
        recipeUnderInspectionViewModel.deleteIngredient(index)
    }

    IngredientForm { handleIngredientAdd(it) }

    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Current ingredients (${recipeUnderInspectionViewModel.recipe.ingredients.size}):",
        style = TextStyle(fontWeight = FontWeight.SemiBold)
    )
    recipeUnderInspectionViewModel.recipe.ingredients.mapIndexed { index, ingredient ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("${ingredient.amount} ${ingredient.unit}   ${ingredient.name}")
            IconButton(
                modifier = Modifier
                    .padding(0.dp)
                    .height(32.dp)
                    .width(32.dp),
                onClick = { handleIngredientDelete(index) }
            ) {
                Icon(
                    Icons.Rounded.Clear,
                    "delete",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .height(24.dp)
                        .width(24.dp)
                )
            }
        }
    }
}

@Composable
private fun InstructionsStep(
    recipeUnderInspectionViewModel: RecipeUnderInspectionViewModel,
    handleAllowNextChange: (Boolean) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val instructions = remember { mutableStateListOf<PersonalInstruction>() }

    LaunchedEffect(Unit) {
        instructions.addAll(recipeUnderInspectionViewModel.recipe.instructions)
    }

    LaunchedEffect(instructions) {
        if (instructions.size > 0) {
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
            val nextNumber =
                if (instructions.isEmpty()) 1
                else instructions.size + 1

            val newInstruction = PersonalInstruction(
                number = nextNumber,
                step = text
            )

            // Setting instructions state
            instructions.add(newInstruction)

            // Setting state in viewModel
            recipeUnderInspectionViewModel.addInstruction(newInstruction)

            // Setting the text-field text state to null
            text = ""
        }
    }

    fun handleIngredientDelete(index: Int) {
        // TODO: Add delete functionality
    }

    Text(
        text = "Instructions",
        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp)
    )

    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = text,
            shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp, bottomEnd = 4.dp),
            onValueChange = { text = it },
            label = { Text("Text") },
            modifier = Modifier
                .weight(0.9f)
                .padding(0.dp)
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
    Text(
        text = "Current instructions",
        style = TextStyle(fontWeight = FontWeight.SemiBold)
    )
    instructions.forEach { instruction ->
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = "${instruction.number}",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = instruction.step, modifier = Modifier.padding(top = 2.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

@Composable
private fun PreviewStep(
    recipeUnderInspectionViewModel: RecipeUnderInspectionViewModel,
    handleAllowNextChange: (Boolean) -> Unit,
    navController: NavController
) {
    var showPreview by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        handleAllowNextChange(true)
    }

    Text(
        text = "Preview & Save",
        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp)
    )
    Spacer(modifier = Modifier.height(16.dp))

    Column {
        Text(
            text = "Title:",
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray.copy(alpha = 0.8f)
            )
        )
        Text(recipeUnderInspectionViewModel.recipe.title)
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
        Text(recipeUnderInspectionViewModel.recipe.image)
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
        Text("${recipeUnderInspectionViewModel.recipe.servings}")
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
            recipeUnderInspectionViewModel.recipe.ingredients.forEach {
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
            recipeUnderInspectionViewModel.recipe.instructions.forEach { instruction ->
                Row {
                    Text("${instruction.number}")
                    Text(instruction.step)
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
            RecipeScreen(navController = navController, preview = true)
        }
    }
}