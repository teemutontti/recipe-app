package com.example.recipeapp.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.composables.NavBar
import com.example.recipeapp.composables.RecipeList
import com.example.recipeapp.composables.TopBar
import com.example.recipeapp.repositories.RecipeRepository
import kotlinx.coroutines.delay

@Composable
fun MyRecipesScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(title = "Recipe App") },
        content = {
            MyRecipesScreenContent(navController = navController, paddingValues = it)
        },
        bottomBar = {
            NavBar(navController = navController, selected = "my_recipes")
        }
    )
}

@Composable
fun MyRecipesScreenContent(navController: NavController, paddingValues: PaddingValues) {
    val context = LocalContext.current
    var loading: Boolean by remember { mutableStateOf(true) }
    var selectedTab: Int by remember { mutableIntStateOf(0) }
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)

    LaunchedEffect(Unit) {
        recipeViewModel.fetchFavourites(context)
        loading = false
    }

    Column(modifier = Modifier.padding(paddingValues)) {
        Column(modifier = Modifier.padding(24.dp)) {
            if (loading) LinearProgressIndicator()
            else {
                TabRow(selectedTabIndex = selectedTab, modifier = Modifier.fillMaxWidth()) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Created") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Favourites") }
                    )
                }
                when (selectedTab) {
                    0 -> MyRecipesCreatedTab(navController)
                    1 -> MyRecipesFavouritesTab(navController)
                }
            }
        }
    }
}

@Composable
fun MyRecipesCreatedTab(navController: NavController) {
    val context = LocalContext.current
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)

    LaunchedEffect(Unit) {
        recipeViewModel.fetchOwnRecipes(context)
    }

    Spacer(modifier = Modifier.height(16.dp))
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        RecipeList(
            navController = navController,
            recipes = recipeViewModel.ownRecipes,
            onEmptyMessage = "You have no created recipes"
        )
    }
}

@Composable
fun MyRecipesFavouritesTab(navController: NavController) {
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)

    Spacer(modifier = Modifier.height(16.dp))
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        RecipeList(
            navController = navController,
            recipes = recipeViewModel.favourites,
            onEmptyMessage = "You have no favourites",
            showAdditionalRecipeInfo = true,
        )
    }
}