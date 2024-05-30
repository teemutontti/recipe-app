package com.example.recipeapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.viewmodels.RecipeUnderInspectionViewModel

/**
 * A composable function that displays a circular button for adding a new recipe.
 * When clicked, it navigates to the recipe editor screen and sets the current recipe in the ViewModel to null.
 *
 * @param navController The NavController used to navigate between screens.
 * @param viewModel The ViewModel that manages the state of the recipe under inspection.
 */
@Composable
fun AddRecipeButton(navController: NavController, viewModel: RecipeUnderInspectionViewModel) {
    Box(
        modifier = Modifier.padding(16.dp)
    ) {
        TextButton(
            shape = CircleShape,
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier.width(64.dp).height(64.dp).clip(CircleShape).shadow(2.dp),
            onClick = {
                viewModel.setRecipe(null)
                navController.navigate("recipe_editor")
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "add",
                modifier = Modifier.width(56.dp).height(56.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}