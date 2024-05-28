package com.example.recipeapp.composables

import android.content.Context
import androidx.activity.ComponentActivity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.ApplicationContext
import com.example.recipeapp.models.SharedPreferencesKeys.PREFS_NAME
import com.example.recipeapp.repositories.RecipeRepository
import com.example.recipeapp.viewmodels.RecipeUnderInspectionViewModel

@Composable
fun AddRecipeButton(navController: NavController) {
    val recipeUnderInspectionViewModel: RecipeUnderInspectionViewModel = viewModel()

    Box(
        modifier = Modifier.padding(16.dp)
    ) {
        TextButton(
            shape = CircleShape,
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier
                .width(64.dp)
                .height(64.dp)
                .clip(CircleShape)
                .shadow(2.dp),
            onClick = {
                recipeUnderInspectionViewModel.setRecipe(null)
                navController.navigate("recipe_editor")
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "add",
                modifier = Modifier
                    .width(56.dp)
                    .height(56.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}