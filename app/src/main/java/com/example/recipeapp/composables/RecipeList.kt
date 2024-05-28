package com.example.recipeapp.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.recipeapp.models.CachedRecipe
import com.example.recipeapp.models.SpoonacularRecipe
import com.example.recipeapp.models.room.FavouriteRecipe
import com.example.recipeapp.models.room.PersonalRecipe

@Composable
fun RecipeList(
    navController: NavController,
    apiRecipes: List<CachedRecipe>? = null,
    ownRecipes: List<PersonalRecipe>? = null,
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