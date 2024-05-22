package com.example.recipeapp

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.repositories.RecipeRepository
import com.example.recipeapp.screens.RecipeEditorScreen
import com.example.recipeapp.screens.DiscoverScreen
import com.example.recipeapp.screens.CookbookScreen
import com.example.recipeapp.screens.RecipeScreen
import com.example.recipeapp.utils.Utils

@Composable
fun App() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)

    LaunchedEffect(Unit) {
        val maxId = recipeViewModel.getOwnRecipesMaxId(context)
        if (maxId != null) {
            Utils.setId(maxId)
        }
    }

    NavHost(navController = navController, startDestination = "discover") {
        composable("discover") {
            DiscoverScreen(navController)
        }
        composable("cookbook") {
            CookbookScreen(navController)
        }
        composable("recipe/{id}") {
            val recipeId: Int? = it.arguments?.getString("id")?.toIntOrNull()
            RecipeScreen(navController, recipeId)
        }
        composable("recipe_editor") {
            RecipeEditorScreen(navController)
        }
    }
}
