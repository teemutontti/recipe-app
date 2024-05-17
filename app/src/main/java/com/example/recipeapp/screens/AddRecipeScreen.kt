package com.example.recipeapp.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.example.recipeapp.composables.TopBar
import com.example.recipeapp.repositories.RecipeRepository

@Composable
fun AddRecipeScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(title = "Add New Recipe") },
        content = {
            AddRecipeScreenContent(navController = navController, paddingValues = it)
        },
    )
}

@Composable
private fun AddRecipeScreenContent(navController: NavController, paddingValues: PaddingValues) {
    val context = LocalContext.current
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)

    val emptyIngredient = Ingredient(
        name = "",
        measures = Measures(
            metric = SingleMeasure(
                amount = 1.0,
                unitShort = ""
            ),
            us = SingleMeasure(
                amount = 1.0,
                unitShort = "",
            )
        ),
    )
    val emptyInstructionStep = Step(
        number = 1,
        step = ""
    )

    var title: String by remember { mutableStateOf("") }
    // TODO: Add image picker option
    //var image: String by remember { mutableStateOf("") }
    var servings: Int by remember { mutableIntStateOf(1) }
    val ingredients = remember { mutableStateListOf(emptyIngredient) }
    val instructions = remember { mutableStateListOf(emptyInstructionStep) }

    fun onRecipeSave() {
        // TODO: Add data validation
        val newRecipe = Recipe(
            id = 123456789,
            title = title,
            image = "",
            servings = servings,
            readyInMinutes = 0,
            license = "",
            sourceName = "",
            sourceUrl = "",
            extendedIngredients = ingredients,
            instructions = instructions.joinToString(". "),
            analyzedInstructions = listOf(Instructions(name = "", steps = instructions))
        )
        recipeViewModel.addOwnRecipe(context, newRecipe)
    }

    Column(modifier = Modifier
        .padding(paddingValues)
        .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            TextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Insert title...") },
                label = { Text("Title *") },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Serving size:")
                TextButton(onClick = { if (servings > 1) servings -= 1 }) {
                    Text("-")
                }
                Text(
                    text = "$servings",
                    modifier = Modifier.width(24.dp),
                    textAlign = TextAlign.Center
                )
                TextButton(onClick = { if (servings < 99) servings += 1 }) {
                    Text("+")
                }
                TextButton(onClick = { servings = 1 }) {
                    Text("Reset")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Ingredients",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            ingredients.forEachIndexed { index, s ->
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ingredient #${index+1}")
                        if (index > 0) IconButton(onClick = { ingredients.removeAt(index) }) {
                            Icon(Icons.Rounded.Clear, "Remove ingredient icon")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = s.name,
                        onValueChange = { ingredients[index] = ingredients[index].copy(name = it) },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        TextField(
                            value = "${s.measures.metric.amount}",
                            onValueChange = { ingredients[index] = ingredients[index].copy(
                                measures = Measures(
                                    metric = SingleMeasure(
                                        amount = it.toFloat(),
                                        unitShort = ingredients[index].measures.metric.unitShort
                                    ),
                                    us = ingredients[index].measures.us
                                )
                            )},
                            label = { Text("Amount") },
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(0.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(
                            value = s.measures.metric.unitShort,
                            onValueChange = { ingredients[index] = ingredients[index].copy(
                                measures = Measures(
                                    metric = SingleMeasure(
                                        amount = ingredients[index].measures.metric.amount,
                                        unitShort = it
                                    ),
                                    us = ingredients[index].measures.us
                                )
                            ) },
                            label = { Text("Unit") },
                            modifier = Modifier.weight(0.5f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            TextButton(onClick = { ingredients.add(emptyIngredient) }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add icon")
                Text("Add Ingredient")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Instructions",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            instructions.forEachIndexed { index, s ->
                TextField(
                    value = s.step,
                    onValueChange = { instructions[index] = instructions[index].copy(
                        number = index + 1,
                        step = it
                    ) },
                    label = { Text("Step ${index+1}") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        if (index > 0) IconButton(onClick = { instructions.removeAt(index) }) {
                            Icon(Icons.Rounded.Clear, "Remove ingredient icon")
                        }
                        else null
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            TextButton(onClick = { instructions.add(emptyInstructionStep) }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add icon")
                Text("Add Instruction")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = { onRecipeSave() }
            ) {
                Text("Save Recipe")
            }
        }
    }
}