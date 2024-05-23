package com.example.recipeapp.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
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
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.ApplicationContext
import com.example.recipeapp.R
import com.example.recipeapp.api.Instructions
import com.example.recipeapp.api.Step
import com.example.recipeapp.composables.IngredientForm
import com.example.recipeapp.composables.NumberCounter
import com.example.recipeapp.composables.RecipeImage
import com.example.recipeapp.composables.StepIndicator
import com.example.recipeapp.repositories.RecipeRepository
import com.example.recipeapp.utils.Utils

@Composable
fun RecipeEditorScreen(navController: NavController) {
    val context = ApplicationContext.current
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)
    var currentFormStep by remember { mutableIntStateOf(0) }
    var allowNext by remember { mutableStateOf(false) }

    fun onSave() {
        // TODO: Move id generation to preview step!!
        recipeViewModel.recipeInAddition?.let {
            // If id is -1 we know that we are editing an existing recipe
            if (it.id == -1) {
                val newId = Utils.getNextId()
                recipeViewModel.addOwnRecipe(context, it.copy(id = newId))
            } else {
                recipeViewModel.updateOwnRecipe(context, it.id.toInt())
            }
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
                        modifier = Modifier
                            .height(16.dp)
                            .width(16.dp)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                if (currentFormStep > 0) {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
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
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
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
                    0 -> TitleStep(handleAllowNextChange, handleStepChange)
                    1 -> IngredientsStep { handleAllowNextChange(it) }
                    2 -> InstructionsStep { handleAllowNextChange(it) }
                    3 -> PreviewStep(handleAllowNextChange, navController)
                }
            }
        }
    }
}

@Composable
private fun TitleStep(handleAllowNextChange: (Boolean) -> Unit, toNextStep: (Int) -> Unit) {
    val context = ApplicationContext.current
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)
    var title by remember { mutableStateOf(recipeViewModel.recipeInAddition?.title ?: "") }
    var servings by remember { mutableIntStateOf(recipeViewModel.recipeInAddition?.servings?.toInt() ?: 1) }
    var imageUri: Uri? by remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                // Persist access to the uri even after app restarts
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                // Update state
                imageUri = it
            }
       },
    )

    fun handlePickImage() {
        launcher.launch("image/*")
    }
    
    fun handleTakePhoto() {
        // TODO: Add functionality
    }

    LaunchedEffect(key1 = title, key2 = servings, key3 = imageUri) {
        if (Utils.Validator.recipeTitle(title) && Utils.Validator.recipeServings(servings)) {
            if (recipeViewModel.recipeInAddition == null) {
                recipeViewModel.setRecipeInAddition(Utils.emptyRecipe)
            }

            recipeViewModel.recipeInAddition?.let {
                recipeViewModel.setRecipeInAddition(
                    it.copy(
                        title = title,
                        servings = servings,
                        image = imageUri.toString()
                    )
                )
            }
            handleAllowNextChange(true)
        }
        Log.d("RecipeEditor", "Image uri: ${imageUri}")
        Log.d("RecipeEditor", "Image uri: ${imageUri.toString()}")
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
            if (imageUri == null) RecipeImage(painter = painterResource(id = R.drawable.meal))
            else RecipeImage(model = imageUri)
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
private fun IngredientsStep(handleAllowNextChange: (Boolean) -> Unit) {
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)
    val ingredients = remember { mutableStateOf(
        recipeViewModel.recipeInAddition?.extendedIngredients ?: listOf()
    )}

    LaunchedEffect(key1 = ingredients.value) {
        if (ingredients.value.isNotEmpty()) {
            handleAllowNextChange(true)
        } else {
            handleAllowNextChange(false)
        }
    }

    fun addIngredient() {
        recipeViewModel.recipeInAddition?.extendedIngredients?.let {
            ingredients.value = it
        }
    }

    fun handleIngredientDelete(index: Int) {
        val newIngredients = ingredients.value.toMutableList()
        newIngredients.removeAt(index)
        ingredients.value = newIngredients

        recipeViewModel.recipeInAddition?.let { recipe ->
            recipeViewModel.setRecipeInAddition(
                recipe.copy(extendedIngredients = newIngredients)
            )
        }
    }

    IngredientForm(::addIngredient)

    Spacer(modifier = Modifier.height(16.dp))
    if (ingredients.value.isNotEmpty()) {
        Text("Current ingredients:", style = TextStyle(fontWeight = FontWeight.SemiBold))
        ingredients.value.forEachIndexed { index, it ->
            key("$index-${it}") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (it != null) {
                        Text("${it.measures.metric.amount} ${it.measures.metric.unitShort}   ${it.name}")
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
        }
    }
}

@Composable
private fun InstructionsStep(handleAllowNextChange: (Boolean) -> Unit) {
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)
    var text by remember { mutableStateOf("") }
    val instructions = remember { mutableStateOf(
        recipeViewModel.recipeInAddition?.analyzedInstructions ?: listOf()
    )}

    LaunchedEffect(key1 = instructions.value) {
        if (instructions.value.isNotEmpty()) {
            if (instructions.value.last().steps.isNotEmpty()) {
                handleAllowNextChange(true)
            }
        }
    }

    fun addInstruction() {
        if (text.isNotEmpty()) {
            // Cleaning up line breaks
            text = text.replace("\n", "")

            // Generating the next steps number
            val nextNumber =
                if (instructions.value.isEmpty()) 1
                else instructions.value.last().steps.lastIndex + 2

            val newInstructions: MutableList<Instructions> = instructions.value.toMutableList()

            // If instructions are found we have to do some more complex logic for adding
            // instruction steps.
            if (newInstructions.size > 0) {
                val lastIndex = newInstructions.lastIndex
                val newSteps: MutableList<Step> = newInstructions[lastIndex].steps.toMutableList()
                newSteps.add(Step(number = nextNumber, step = text))
                newInstructions[lastIndex] = newInstructions[lastIndex].copy(steps = newSteps)
            } else {
                newInstructions.add(
                    Instructions(name = "", steps = listOf(Step(number = nextNumber, step = text)))
                )
            }

            // Setting instructions state
            instructions.value = newInstructions

            // Saving analyzedInstructions in viewModel to persist the data on step navigations
            recipeViewModel.recipeInAddition?.let {
                recipeViewModel.setRecipeInAddition(it.copy(analyzedInstructions = newInstructions))
            }

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
    instructions.value.forEachIndexed { index1, instruction ->
        Column {
            Text(
                text = "Current instructions${if (instruction.name.isNotEmpty()) " (${instruction.name}):" else ":"}",
                style = TextStyle(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            instruction.steps.forEachIndexed { index2, step ->
                Row {
                    Text(
                        text = "${step.number}",
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
                    Text(text = step.step, modifier = Modifier.padding(top = 2.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun PreviewStep(handleAllowNextChange: (Boolean) -> Unit, navController: NavController) {
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)
    var showPreview by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        recipeViewModel.recipeInAddition?.let {
            recipeViewModel.setSelectedRecipe(it)
            handleAllowNextChange(true)
        }
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
        Text("${recipeViewModel.recipeInAddition?.title}")
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
        Text("${recipeViewModel.recipeInAddition?.image}")
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
        Text("${recipeViewModel.recipeInAddition?.servings}")
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
            recipeViewModel.recipeInAddition?.extendedIngredients?.forEach {
                Text("${it.measures.metric.amount} ${it.measures.metric.unitShort}  ${it.name}")
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
            recipeViewModel.recipeInAddition?.analyzedInstructions?.forEach { it1 ->
                it1.steps.forEach { it2 ->
                    Row {
                        Text("${it2.number}")
                        Text(it2.step)
                    }
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