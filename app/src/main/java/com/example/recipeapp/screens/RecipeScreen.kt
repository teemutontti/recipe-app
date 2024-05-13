package com.example.recipeapp.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipeapp.api.Instructions
import com.example.recipeapp.api.Recipe
import com.example.recipeapp.composables.IngredientLine
import com.example.recipeapp.repositories.RecipeRepository

@Composable
fun RecipeScreen(id: Int?) {
    var loading by remember { mutableStateOf(true) }
    var recipe: Recipe? by remember { mutableStateOf(null) }
    var instructions: List<Instructions>? by remember { mutableStateOf(null) }

    val context = LocalContext.current
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)

    LaunchedEffect(key1 = id) {
        if (id != null) {
            recipe = recipeViewModel.fetchRecipeById(context, id)
            if (recipe != null) {
                instructions = recipeViewModel.fetchRecipeInstructions(context, recipe!!.id.toInt())
            }
        }
        loading = false
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.padding(horizontal = 25.dp, vertical = 35.dp)) {
            Text(
                text = recipe?.title ?: "Loading...",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(50.dp))
            Column(horizontalAlignment = Alignment.Start) {
                if (loading) {
                    CircularProgressIndicator()
                } else if (recipe != null) {
                    Text(text = "For ${recipe?.servings} people")
                    Text(
                        text = "Ingredients",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Column {
                        recipe?.extendedIngredients?.forEach {
                            IngredientLine(ingredient = it)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Instructions",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Column {
                        instructions?.forEach {it1 ->
                            Text(text = it1.name)
                            Row {
                                it1.steps.forEach {it2 ->
                                    Text(
                                        text = "${it2.number}",
                                        style = TextStyle(
                                            fontSize = 25.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.secondary,
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(text = it2.step)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}