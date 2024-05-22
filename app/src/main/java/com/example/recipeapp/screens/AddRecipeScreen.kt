package com.example.recipeapp.screens

import android.util.Log
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.api.Ingredient
import com.example.recipeapp.api.Instructions
import com.example.recipeapp.api.Measures
import com.example.recipeapp.api.Recipe
import com.example.recipeapp.api.SingleMeasure
import com.example.recipeapp.api.Step
import com.example.recipeapp.composables.IngredientForm
import com.example.recipeapp.composables.NumberCounter
import com.example.recipeapp.composables.StepIndicator
import com.example.recipeapp.composables.TopBar
import com.example.recipeapp.repositories.RecipeRepository
import com.example.recipeapp.utils.AddableIngredient
import com.example.recipeapp.utils.Utils

@Composable
fun AddRecipeScreen(navController: NavController) {
    var currentFormStep by remember { mutableIntStateOf(0) }
    var allowNext by remember { mutableStateOf(false) }

    fun handleStepChange(direction: Int) {
        val newCurrentFormStep = currentFormStep + direction
        if (newCurrentFormStep in 0..3) {
            currentFormStep = newCurrentFormStep
        }
        if (direction < 0) {
            allowNext = true
        } else {
            allowNext = false
        }
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
            AddRecipeScreenContent(
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
                    Text("Next")
                }
            }
        }
    )
}

@Composable
private fun AddRecipeScreenContent(
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
                    3 -> PreviewStep { handleAllowNextChange(it) }
                }
            }
        }
    }
}

@Composable
private fun TitleStep(handleAllowNextChange: (Boolean) -> Unit, toNextStep: (Int) -> Unit) {
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)
    var title by remember { mutableStateOf(recipeViewModel.recipeInAddition?.title ?: "") }

    fun handleTitleChange(newTitle: String) {
        title = newTitle

        if (recipeViewModel.recipeInAddition == null) {
            recipeViewModel.setRecipeInAddition(Utils.emptyRecipe)
        }

        recipeViewModel.recipeInAddition?.let {
            recipeViewModel.setRecipeInAddition(it.copy(title = newTitle))
            handleAllowNextChange(true)
        }
    }

    LaunchedEffect(key1 = title) {
        if (title.length > 2) handleAllowNextChange(true)
    }

    Text(
        text = "Title & Image",
        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp)
    )

    Spacer(modifier = Modifier.height(16.dp))
    TextField(
        value = title,
        onValueChange = ::handleTitleChange,
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
}

@Composable
private fun IngredientsStep(handleAllowNextChange: (Boolean) -> Unit) {
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)
    val ingredients = remember { mutableStateOf(
        recipeViewModel.recipeInAddition?.extendedIngredients ?: listOf()
    )}

    fun addIngredient() {
        recipeViewModel.ingredientInAddition?.let {
            val newIngredient: Ingredient = Utils.emptyIngredient.copy(
                name = it.name,
                measures = Utils.emptyIngredient.measures.copy(
                    metric = Utils.emptyIngredient.measures.metric.copy(
                        amount = it.amount,
                        unitShort = it.unit
                    )
                )
            )
            val newIngredients = ingredients.value.toMutableList()
            newIngredients.add(newIngredient)
            ingredients.value = newIngredients

            recipeViewModel.recipeInAddition?.let { recipe ->
                recipeViewModel.setRecipeInAddition(
                    recipe.copy(extendedIngredients = newIngredients)
                )
            }
        }
    }

    fun handleIngredientDelete(index: Int) {
        val newIngredients = ingredients.value.toMutableList()
        newIngredients.removeAt(index)
        ingredients.value = newIngredients
    }

    IngredientForm( {handleAllowNextChange(it) }, ::addIngredient)

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
                            modifier = Modifier.padding(0.dp).height(32.dp).width(32.dp),
                            onClick = { handleIngredientDelete(index) }
                        ) {
                            Icon(
                                Icons.Rounded.Clear,
                                "delete",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.height(24.dp).width(24.dp)
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
    Text(
        text = "Instructions",
        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp)
    )
}

@Composable
private fun PreviewStep(handleAllowNextChange: (Boolean) -> Unit) {
    Text(
        text = "Preview & Save",
        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp)
    )
}