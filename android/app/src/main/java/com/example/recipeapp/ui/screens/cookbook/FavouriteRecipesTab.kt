package com.example.recipeapp.ui.screens.cookbook

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.recipeapp.ui.components.layout.RecipeList
import com.example.recipeapp.ui.components.misc.UserFeedbackMessage
import com.example.recipeapp.viewmodels.ViewModelWrapper


/**
 * Composable function for displaying favourite recipes tab.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
internal fun FavouriteRecipesTab(navController: NavController, viewModels: ViewModelWrapper) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        if (viewModels.personal.alert != null) {
            UserFeedbackMessage(viewModels.personal.alert!!.message, "error")
        } else if (viewModels.favourite.loading) {
            LinearProgressIndicator()
        } else {
            RecipeList(navController, viewModels.favourite.recipes, viewModels)
        }
    }
}