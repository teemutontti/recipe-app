package com.example.recipeapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import com.example.recipeapp.utils.FormattingUtils
import com.example.recipeapp.viewmodels.LogsScreenViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
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
    mode: String,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModels.logsScreen.alert) {
        viewModels.logsScreen.alert?.let {
            snackbarHostState.showSnackbar(it.message)
            viewModels.logsScreen.clearAlert()
        }
    }

    Scaffold(
        topBar = { TopBar(subtitle = { BackButton(navController) }) },
        content = {
            when (mode) {
                "ADD" -> AddMode(it, viewModels.logsScreen)
                "EDIT" -> EditMode(it, viewModels.logsScreen)
                "UPDATE" -> EditMode(it, viewModels.logsScreen)
                else -> ViewMode(it, viewModels.logsScreen)
            }
        },
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
                                val log = it.id?.let { it1 ->
                                    Log(
                                        date = viewModels.logsScreen.date.toString(),
                                        // TODO: Make time changeable
                                        time = FormattingUtils.formatLocalTimeToString(
                                            LocalTime.now()
                                        ),
                                        meal = viewModels.logsScreen.selectedMeal.toString(),
                                        userId = 1,
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
        snackbarHost = { SnackbarHost(snackbarHostState) }
    )
}

@Composable
private fun AddMode(
    paddingValues: PaddingValues,
    viewModel: LogsScreenViewModel
) {
    Column(modifier = Modifier.padding(paddingValues)) {
        Column(modifier = Modifier.padding(horizontal = 40.dp)) {
            Text("Add food", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(24.dp))
            FoodForm(viewModel)
        }
    }
}

@Composable
private fun EditMode(
    paddingValues: PaddingValues,
    viewModel: LogsScreenViewModel,
) {
    var calculatedNutrientSummary by remember { mutableStateOf(NutrientSummary(0.0, 0.0, 0.0, 0.0)) }

    LaunchedEffect(viewModel.currentAmount) {
        if (viewModel.selectedFood != null && viewModel.currentAmount.isNotEmpty()) {
            val multiplier = (viewModel.currentAmount.toDouble() / 100)

            val calories = viewModel.selectedFood!!.calories * multiplier
            val fats = viewModel.selectedFood!!.fat * multiplier
            val carbs = viewModel.selectedFood!!.carbs * multiplier
            val protein = viewModel.selectedFood!!.protein * multiplier

            calculatedNutrientSummary = NutrientSummary(calories, fats, carbs, protein)
        }
    }

    Column(modifier = Modifier.padding(paddingValues)) {
        Column(modifier = Modifier.padding(horizontal = 40.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "${viewModel.selectedFood?.name}",
                    style = MaterialTheme.typography.headlineLarge
                )
                // TODO: Add functionality to request update
                // TODO: Add functionality to request delete
            }
            Text(
                text = "${viewModel.selectedFood?.barcode}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
            )
            Spacer(modifier = Modifier.padding(vertical = 24.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                NutrientColumn(
                    nutrient = "Calories",
                    value = calculatedNutrientSummary.calories,
                    suffix = "kcal",
                )
                NutrientTextField(value = viewModel.currentAmount) {
                    viewModel.setCurrentAmount(it)
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Row {
                NutrientColumn(
                    nutrient = "Carbs",
                    value = calculatedNutrientSummary.carbs,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                NutrientColumn(
                    nutrient = "Protein",
                    value = calculatedNutrientSummary.protein,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                NutrientColumn(
                    nutrient = "Fat",
                    value = calculatedNutrientSummary.fats,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 24.dp))
            TitledContainer(
                title = "Nutrients per 100 g",
                titleSize = MaterialTheme.typography.headlineSmall,
                backgroundColor = MaterialTheme.colorScheme.background,
            ) {
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    NutrientInputRow(
                        "Calories *",
                        viewModel.selectedFood?.calories.toString(),
                        120.dp,
                        "kcal",
                        editable = false
                    )
                    Spacer(Modifier.padding(vertical = 4.dp))
                    NutrientInputRow(
                        "Carbohydrates",
                        viewModel.selectedFood?.carbs.toString(),
                        suffixText = "g",
                        editable = false
                    )
                    Spacer(Modifier.padding(vertical = 4.dp))
                    NutrientInputRow(
                        "Protein",
                        viewModel.selectedFood?.protein.toString(),
                        suffixText = "g",
                        editable = false
                    )
                    Spacer(Modifier.padding(vertical = 4.dp))
                    NutrientInputRow(
                        "Fat",
                        viewModel.selectedFood?.fat.toString(),
                        suffixText = "g",
                        editable = false
                    )
                }
            }
        }
    }
}

@Composable
private fun ViewMode(
    paddingValues: PaddingValues,
    viewModel: LogsScreenViewModel,
) {
    Column(modifier = Modifier.padding(paddingValues)) {
        Column(modifier = Modifier.padding(horizontal = 40.dp)) {
            Text("Add food", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(24.dp))
            FoodForm(viewModel)
        }
    }
}
