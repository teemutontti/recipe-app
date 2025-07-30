package com.example.recipeapp.ui.screens.food

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.recipeapp.models.Log
import com.example.recipeapp.ui.components.buttons.BackButton
import com.example.recipeapp.ui.components.layout.TopBar
import com.example.recipeapp.viewmodels.ViewModelWrapper
import com.example.recipeapp.models.NutrientSummary
import com.example.recipeapp.ui.components.inputs.CancelSaveOption
import com.example.recipeapp.ui.components.inputs.FoodForm
import com.example.recipeapp.ui.components.inputs.NutrientInputRow
import com.example.recipeapp.ui.components.inputs.NutrientTextField
import com.example.recipeapp.ui.components.layout.NutrientColumn
import com.example.recipeapp.ui.components.layout.TitledContainer
import com.example.recipeapp.ui.screens.Screen
import com.example.recipeapp.utils.Constants
import com.example.recipeapp.utils.FormattingUtils
import com.example.recipeapp.viewmodels.LogsScreenViewModel
import java.time.LocalTime

/**
 * Composable function for displaying the Food Editor screen.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
fun FoodEditorScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
    snackbarHostState: SnackbarHostState,
    mode: String,
) {
    LaunchedEffect(viewModels.logsScreen.alert) {
        viewModels.logsScreen.alert?.let {
            snackbarHostState.showSnackbar(it.message)
            viewModels.logsScreen.clearAlert()
        }
    }

    Screen(
        topBar = { TopBar(subtitle = { BackButton(navController) }) },
        bottomBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                when (mode) {
                    "ADD" ->
                        CancelSaveOption(onClose = { navController.navigateUp() }) {
                            viewModels.logsScreen.savableFood?.let {
                                viewModels.logsScreen.saveFood(it)
                                navController.navigateUp()
                            }
                        }
                    "EDIT" ->
                        CancelSaveOption(onClose = { navController.navigateUp() }) {
                            viewModels.logsScreen.selectedFood?.let {
                                val log = it.food?.id?.let { it1 ->
                                    Log(
                                        date = viewModels.logsScreen.date.toString(),
                                        // TODO: Make time changeable
                                        time = FormattingUtils.formatLocalTimeToString(
                                            LocalTime.now()
                                        ),
                                        meal = viewModels.logsScreen.selectedMeal.toString(),
                                        userId = viewModels.logsScreen.getUserId(),
                                        foodId = it1,
                                        amount = viewModels.logsScreen.currentAmount.toDouble(),
                                    )
                                }
                                if (log != null) {
                                    viewModels.logsScreen.saveLog(log)
                                    repeat(2) { navController.popBackStack() }
                                }
                            }
                        }
                    "UPDATE" ->
                        CancelSaveOption(onClose = { navController.navigateUp() }) {
                            val newAmount = viewModels.logsScreen.currentAmount.toDoubleOrNull()
                            if (newAmount != null) {
                                val updatedLog = viewModels.logsScreen.selectedLog?.copy(amount = newAmount)
                                if (updatedLog != null) {
                                    viewModels.logsScreen.updateLog(updatedLog)
                                    navController.popBackStack()
                                }
                            }
                        }
                    else -> null
                }
            }
        },
        screen = Constants.Screen.FOOD_EDIT,
        viewModels,
        navController,
    ) {
        when (mode) {
            "ADD" -> AddMode(navController, viewModels)
            "EDIT" -> EditMode(viewModels.logsScreen)
            "UPDATE" -> EditMode(viewModels.logsScreen)
            else -> ViewMode(navController, viewModels)
        }
    }
}
