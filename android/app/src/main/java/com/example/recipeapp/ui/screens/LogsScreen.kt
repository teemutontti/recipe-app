package com.example.recipeapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.ui.components.misc.MacroWheel
import com.example.recipeapp.ui.components.navigation.NavBar
import com.example.recipeapp.ui.components.layout.TopBar
import com.example.recipeapp.viewmodels.ViewModelWrapper

/**
 * Composable function for displaying the Logs screen.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
fun LogsScreen(navController: NavController, viewModels: ViewModelWrapper) {
    Scaffold(
        topBar = { TopBar("Logs") },
        content = { LogsScreenContent(navController, viewModels, it) },
        bottomBar = { NavBar(navController, "cookbook") }
    )
}

/**
 * Composable function for the content of the Logs screen.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 * @param paddingValues Padding values for the content.
 */
@Composable
private fun LogsScreenContent(
    navController: NavController,
    viewModels: ViewModelWrapper,
    paddingValues: PaddingValues,
) {
    Column(modifier = Modifier.padding(paddingValues)) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                MacroWheel(1500, "Calories", MaterialTheme.colorScheme.secondary)
                Row {
                    MacroWheel(50, "Fats", MaterialTheme.colorScheme.errorContainer, "small")
                    MacroWheel(180, "Carbohydrates", MaterialTheme.colorScheme.primary, "small")
                    MacroWheel(150, "Protein", MaterialTheme.colorScheme.tertiary, "small")
                }
            }
        }
    }
}
