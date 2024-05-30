package com.example.recipeapp.composables

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.models.Recipe
import com.example.recipeapp.models.room.PersonalRecipe
import com.example.recipeapp.viewmodels.ViewModelWrapper

@Composable
fun RecipeList(
    navController: NavController,
    recipes: List<Recipe>,
    viewModels: ViewModelWrapper,
    onEmpty: @Composable () -> Unit = {},
) {
    LaunchedEffect(Unit) {
        Log.d("RecipeList", "Recipes: ${recipes.size}")
    }

    if (recipes.isEmpty()) onEmpty()
    else {
        recipes.reversed().map {
            RecipeButton(navController, it, viewModels.inspection)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
