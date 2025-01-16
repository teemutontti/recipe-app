package com.example.recipeapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.example.recipeapp.models.FoodLog
import com.example.recipeapp.models.MealType
import com.example.recipeapp.models.NutrientSummary
import com.example.recipeapp.ui.components.buttons.AddButton
import com.example.recipeapp.ui.components.buttons.BackButton
import com.example.recipeapp.ui.components.buttons.MealLogButton
import com.example.recipeapp.ui.components.dialogs.SharedSnackbar
import com.example.recipeapp.ui.components.layout.MealNutrients
import com.example.recipeapp.ui.components.layout.NutrientColumn
import com.example.recipeapp.ui.components.layout.TopBar
import com.example.recipeapp.utils.FormattingUtils.toLowerCaseCapitalizeFirst
import com.example.recipeapp.viewmodels.ViewModelWrapper

@Composable
fun MealScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
    mealType: MealType,
) {
    Scaffold(
        topBar = { TopBar(subtitle = { BackButton(navController) }) },
        content = {
            MealScreenContent(
                navController = navController,
                paddingValues = it,
                viewModels = viewModels,
                mealType = mealType,
            )
        },
        snackbarHost = { SharedSnackbar(viewModels.logsScreen) }
    )
}

@Composable
private fun MealScreenContent(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModels: ViewModelWrapper,
    mealType: MealType,
) {
    var logs: List<FoodLog> by remember { mutableStateOf(emptyList()) }

    LaunchedEffect(mealType) {
        logs = when (mealType) {
            MealType.BREAKFAST -> viewModels.logsScreen.breakfastLogs
            MealType.LUNCH -> viewModels.logsScreen.lunchLogs
            MealType.DINNER -> viewModels.logsScreen.dinnerLogs
            else -> viewModels.logsScreen.snacksLogs
        }
    }

    Box(modifier = Modifier.padding(paddingValues).fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        Column(modifier = Modifier.padding(horizontal = 40.dp).fillMaxSize()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = toLowerCaseCapitalizeFirst(mealType.toString()),
                    style = MaterialTheme.typography.headlineLarge,
                )
                viewModels.logsScreen.nutrients[mealType]?.let {
                    NutrientColumn("Calories", it.calories, suffix = "kcal")
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            viewModels.logsScreen.nutrients[mealType]?.let {
                MealNutrients(it.carbs, it.protein, it.fats)
            }
            Spacer(modifier = Modifier.padding(vertical = 16.dp))
            LazyColumn {
                items(logs) {
                    MealLogButton(it)
                }
            }
        }
        AddButton { navController.navigate("add_food") }
    }
}
