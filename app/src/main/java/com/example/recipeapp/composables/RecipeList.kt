package com.example.recipeapp.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.api.Recipe
import com.example.recipeapp.repositories.RecipeRepository
import com.example.recipeapp.utils.CachedRecipe

@Composable
fun RecipeList(
    navController: NavController,
    apiRecipes: List<CachedRecipe>? = null,
    ownRecipes: List<Recipe>? = null,
    onEmpty: @Composable () -> Unit = {},
) {
    if (apiRecipes != null) {
        if (apiRecipes.isNotEmpty()) {
            apiRecipes.reversed().map {
                RecipeButton(
                    navController = navController,
                    apiRecipe = it,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            onEmpty()
        }
    } else if (ownRecipes != null) {
        if (ownRecipes.isNotEmpty()) {
            ownRecipes.reversed().map {
                RecipeButton(
                    navController = navController,
                    ownRecipe = it,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            onEmpty()
        }
    }
}