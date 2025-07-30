package com.example.recipeapp.ui.screens.cookbook

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.ui.components.buttons.AddRecipeButton
import com.example.recipeapp.ui.components.layout.RecipeList
import com.example.recipeapp.ui.components.misc.EmptyRecipeList
import com.example.recipeapp.ui.components.misc.UserFeedbackMessage
import com.example.recipeapp.viewmodels.ViewModelWrapper

/**
 * Composable function for displaying personal recipes tab.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
internal fun PersonalRecipesTab(navController: NavController, viewModels: ViewModelWrapper) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            if (viewModels.personal.alert != null) {
                UserFeedbackMessage(viewModels.personal.alert!!.message, "error")
            } else if (viewModels.personal.loading) {
                LinearProgressIndicator()
            } else {
                RecipeList(navController, viewModels.personal.recipes, viewModels,
                    onEmpty = { EmptyRecipeList(navController, viewModels) }
                )
            }
        }
    }
}