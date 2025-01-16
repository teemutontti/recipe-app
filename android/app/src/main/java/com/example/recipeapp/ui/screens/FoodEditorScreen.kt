package com.example.recipeapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.ui.components.buttons.BackButton
import com.example.recipeapp.ui.components.layout.TopBar
import com.example.recipeapp.viewmodels.ViewModelWrapper
import com.example.recipeapp.models.Food
import com.example.recipeapp.ui.components.dialogs.SharedSnackbar
import com.example.recipeapp.ui.components.inputs.FoodForm

/**
 * Composable function for displaying the Food Editor screen.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
fun FoodEditorScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
) {
    Scaffold(
        topBar = { TopBar(subtitle = { BackButton(navController) }) },
        content = {
            FoodEditorContent(
                navController = navController,
                paddingValues = it,
                viewModels = viewModels,
            )
        },
        bottomBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    onClick = { navController.navigateUp() },
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.surface),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    onClick = {
                        val food: Food? = viewModels.logsScreen.savableFood
                        if (food != null) {
                            viewModels.logsScreen.saveFood(food)
                            navController.navigateUp()
                        }
                    },
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.surface),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                ) {
                    Text("Save")
                }
            }
        },
        snackbarHost = { SharedSnackbar(viewModels.logsScreen) }
    )
}

/**
 * Composable function for the content of the Food Editor screen.
 * @param navController The navigation controller for navigating between screens.
 * @param paddingValues Padding values for the content.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
private fun FoodEditorContent(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModels: ViewModelWrapper,
) {
    Column(modifier = Modifier.padding(paddingValues)) {
        Column(modifier = Modifier.padding(horizontal = 40.dp)) {
            Text("Add food", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(24.dp))
            FoodForm(viewModels.logsScreen)
        }
    }
}
