package com.example.recipeapp.composables

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.api.Recipe
import com.example.recipeapp.repositories.RecipeRepository

@Composable
fun RecipeList(
    navController: NavController,
    recipes: List<Recipe>,
    onEmptyMessage: String = "",
    showAdditionalRecipeInfo: Boolean = false
) {
    if (recipes.isNotEmpty()) {
        recipes.map {
            RecipeButton(
                navController = navController,
                recipe = it,
                showAdditionalInfo = showAdditionalRecipeInfo
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    } else {
        Text(text = onEmptyMessage)
    }
}