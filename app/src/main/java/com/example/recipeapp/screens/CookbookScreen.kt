package com.example.recipeapp.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.api.CachedRecipe
import com.example.recipeapp.composables.NavBar
import com.example.recipeapp.composables.RecipeList
import com.example.recipeapp.composables.TopBar
import com.example.recipeapp.repositories.RecipeRepository

@Composable
fun CookbookScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(title = "Cookbook") },
        content = {
            CookbookScreenContent(navController = navController, paddingValues = it)
        },
        bottomBar = {
            NavBar(navController = navController, selected = "cookbook")
        }
    )
}

@Composable
fun CookbookScreenContent(navController: NavController, paddingValues: PaddingValues) {
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
                0 -> OwnRecipesTab(navController)
                1 -> SavedRecipesTab(navController)
            }
        }
    }
}

@Composable
fun OwnRecipesTab(navController: NavController) {
    val context = LocalContext.current
    var loading: Boolean by remember { mutableStateOf(true) }
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)

    LaunchedEffect(Unit) {
        recipeViewModel.fetchOwnRecipes(context)
        loading = false
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Button(onClick = { navController.navigate("add_recipe") }) {
            Text("Add New Recipe")
        }
        RecipeList(
            navController = navController,
            recipes = recipeViewModel.ownRecipes.map {
                CachedRecipe(
                    id = it.id,
                    image = it.image,
                    title = it.title
                )
            }
        )
    }
}

@Composable
fun SavedRecipesTab(navController: NavController) {
    val context = LocalContext.current
    var loading: Boolean by remember { mutableStateOf(true) }
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)

    LaunchedEffect(Unit) {
        recipeViewModel.fetchFavourites(context)
        loading = false
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        RecipeList(navController = navController, recipes = recipeViewModel.favourites)
    }
}