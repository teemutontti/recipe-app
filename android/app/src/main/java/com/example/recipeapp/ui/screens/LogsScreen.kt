package com.example.recipeapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.models.MealType
import com.example.recipeapp.ui.components.buttons.AddButton
import com.example.recipeapp.ui.components.buttons.MealButton
import com.example.recipeapp.ui.components.inputs.DateNavigator
import com.example.recipeapp.ui.components.layout.MacroWheels
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
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModels.logsScreen.alert) {
        viewModels.logsScreen.alert?.let {
            snackbarHostState.showSnackbar(it.message)
            viewModels.logsScreen.clearAlert()
        }
    }

    Scaffold(
        topBar = { TopBar("Logs") },
        content = { LogsScreenContent(navController, viewModels, it) },
        bottomBar = { NavBar(navController, "logs") },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = { AddButton { navController.navigate("add_food") } }
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

    LaunchedEffect(Unit) {
        viewModels.logsScreen.loadLogs()
    }

    Column(modifier = Modifier.padding(paddingValues)) {
        LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DateNavigator(viewModels.logsScreen.date) {
                            viewModels.logsScreen.setDate(it)
                        }
                        MacroWheels(viewModels.logsScreen.overallNutrients)
                        Spacer(modifier = Modifier.padding(vertical = 16.dp))
                    }
                }
            }
            item {
                MealButton(MealType.BREAKFAST, viewModels.logsScreen) {
                    viewModels.logsScreen.setSelectedMeal(MealType.BREAKFAST)
                    navController.navigate("meal")
                }
            }
            item {
                MealButton(MealType.LUNCH, viewModels.logsScreen) {
                    viewModels.logsScreen.setSelectedMeal(MealType.LUNCH)
                    navController.navigate("meal")
                }
            }
            item {
                MealButton(MealType.DINNER, viewModels.logsScreen) {
                    viewModels.logsScreen.setSelectedMeal(MealType.DINNER)
                    navController.navigate("meal")
                }
            }
            item {
                MealButton(MealType.SNACKS, viewModels.logsScreen) {
                    viewModels.logsScreen.setSelectedMeal(MealType.SNACKS)
                    navController.navigate("meal")
                }
            }
        }
    }
}
