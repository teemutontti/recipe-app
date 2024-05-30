package com.example.recipeapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.ui.components.AddRecipeButton
import com.example.recipeapp.ui.components.NavBar
import com.example.recipeapp.ui.components.RecipeList
import com.example.recipeapp.ui.components.TopBar
import com.example.recipeapp.ui.components.UserFeedbackMessage
import com.example.recipeapp.viewmodels.ViewModelWrapper

/**
 * Composable function for displaying the Cookbook screen.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
fun CookbookScreen(navController: NavController, viewModels: ViewModelWrapper) {
    Scaffold(
        topBar = { TopBar(title = "Cookbook") },
        content = { CookbookScreenContent(navController, viewModels, it) },
        bottomBar = { NavBar(navController, "cookbook") }
    )
}

/**
 * Composable function for the content of the Cookbook screen.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 * @param paddingValues Padding values for the content.
 */
@Composable
private fun CookbookScreenContent(
    navController: NavController,
    viewModels: ViewModelWrapper,
    paddingValues: PaddingValues,
) {
    var selectedTab: Int by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.padding(paddingValues)) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            TabRow(selectedTabIndex = selectedTab, modifier = Modifier.fillMaxWidth()) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("My Recipes") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Saved Recipes") }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            when (selectedTab) {
                0 -> PersonalRecipesTab(navController, viewModels)
                1 -> FavouriteRecipesTab(navController, viewModels)
            }
        }
    }
}

/**
 * Composable function for displaying personal recipes tab.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
private fun PersonalRecipesTab(navController: NavController, viewModels: ViewModelWrapper) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            if (viewModels.personal.error != null) {
                UserFeedbackMessage(viewModels.personal.error!!, "error")
            } else if (viewModels.personal.loading) {
                LinearProgressIndicator()
            } else {
                RecipeList(navController, viewModels.personal.recipes, viewModels,
                    onEmpty = {
                        Column {
                            UserFeedbackMessage(message = "Seem like you have no recipes yet.")
                            Row {
                                Spacer(modifier = Modifier.width(16.dp))
                                Button(
                                    shape = RoundedCornerShape(8.dp),
                                    onClick = {
                                        viewModels.inspection.setRecipe(null)
                                        navController.navigate("recipe_editor")
                                    }
                                ) {
                                    Text("Let's change that!")
                                }
                            }
                        }
                    }
                )
            }
        }
        AddRecipeButton(navController, viewModels.inspection)
    }
}

/**
 * Composable function for displaying favourite recipes tab.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
private fun FavouriteRecipesTab(navController: NavController, viewModels: ViewModelWrapper) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        if (viewModels.favourite.error != null) {
            UserFeedbackMessage(viewModels.personal.error!!, "error")
        } else if (viewModels.favourite.loading) {
            LinearProgressIndicator()
        } else {
            RecipeList(navController, viewModels.favourite.recipes, viewModels)
        }
    }
}
