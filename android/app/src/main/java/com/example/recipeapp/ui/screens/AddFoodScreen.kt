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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.ui.components.buttons.BackButton
import com.example.recipeapp.ui.components.layout.TopBar
import com.example.recipeapp.viewmodels.ViewModelWrapper
import com.example.recipeapp.ui.components.inputs.CustomSearchBar
import com.example.recipeapp.ui.components.misc.ItemDivider

@Composable
fun AddFoodScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
) {
    Scaffold(
        topBar = { TopBar(subtitle = { BackButton(navController) }) },
        content = {
            AddFoodContent(
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
                    onClick = {},
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.surface),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                ) {
                    Text("Save")
                }
            }
        }
    )
}

/**
 * Composable function for the content of the Log Editor screen.
 * @param navController The navigation controller for navigating between screens.
 * @param paddingValues Padding values for the content.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
private fun AddFoodContent(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModels: ViewModelWrapper,
) {
    var showResult by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(paddingValues)) {
        Column(modifier = Modifier.padding(horizontal = 40.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Add foods", style = MaterialTheme.typography.headlineLarge)
                Row {
                    TextButton(onClick = { navController.navigate("food_editor") }) {
                        Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "Add")
                        Text(text = "Add food")
                    }
                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            CustomSearchBar(
                placeholder = "Search for foods",
                showResult = showResult,
                handleShowResult = { showResult = it },
                autoSearch = true,
                onClear = { viewModels.logsScreen.loadFoods() },
                search = { viewModels.logsScreen.searchFoods(it) },
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                itemsIndexed(viewModels.logsScreen.foods) {index, food ->
                    Text(text = food.name)
                    if (index < viewModels.logsScreen.foods.size - 1) ItemDivider()
                }
            }
        }
    }
}
