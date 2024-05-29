package com.example.recipeapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.composables.AddRecipeButton
import com.example.recipeapp.composables.NavBar
import com.example.recipeapp.composables.RecipeList
import com.example.recipeapp.composables.TopBar
import com.example.recipeapp.models.CachedRecipe
import com.example.recipeapp.viewmodels.FavouriteRecipesViewModel
import com.example.recipeapp.viewmodels.PersonalRecipesViewModel
import com.example.recipeapp.viewmodels.RecipeUnderInspectionViewModel

@Composable
fun CookbookScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(title = "Cookbook") },
        content = { CookbookScreenContent(navController, it) },
        bottomBar = { NavBar(navController, "cookbook") }
    )
}

@Composable
private fun CookbookScreenContent(navController: NavController, paddingValues: PaddingValues) {
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
                0 -> PersonalRecipesTab(navController)
                1 -> FavouriteRecipesTab(navController)
            }
        }
    }
}

@Composable
private fun PersonalRecipesTab(navController: NavController) {
    val personalRecipesViewModel: PersonalRecipesViewModel = viewModel()
    val recipeInEditingViewModel: RecipeUnderInspectionViewModel = viewModel()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            if (personalRecipesViewModel.loading) {
                LinearProgressIndicator()
            } else {
                RecipeList(
                    navController = navController,
                    ownRecipes = personalRecipesViewModel.recipes,
                    onEmpty = {
                        Column {
                            Text(text = "You have no recipes yet...")
                            Button(onClick = {
                                recipeInEditingViewModel.setRecipe(null)
                                navController.navigate("recipe_editor")
                            }) {
                                Text(text = "Let's change that!")
                            }
                        }
                    }
                )
            }
        }
        AddRecipeButton(navController = navController)
    }
}

@Composable
private fun FavouriteRecipesTab(navController: NavController) {
    val favouriteRecipesViewModel: FavouriteRecipesViewModel = viewModel()

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        RecipeList(
            navController = navController,
            apiRecipes = favouriteRecipesViewModel.recipes.map {
                CachedRecipe(
                    id = it.id,
                    image = it.image,
                    title = it.title,
                )
            }
        )
    }
}
