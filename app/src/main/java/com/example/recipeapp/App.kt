package com.example.recipeapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.screens.AddRecipeScreen
import com.example.recipeapp.screens.DiscoverScreen
import com.example.recipeapp.screens.CookbookScreen
import com.example.recipeapp.screens.RecipeScreen
import com.example.recipeapp.screens.SearchResultsScreen

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "discover") {
        composable("discover") {
            DiscoverScreen(navController)
        }
        composable("search_results/{query}") {
            val query: String? = it.arguments?.getString("query")
            SearchResultsScreen(navController, query)
        }
        composable("cookbook") {
            CookbookScreen(navController)
        }
        composable("recipe/{id}") {
            val recipeId: Int? = it.arguments?.getString("id")?.toIntOrNull()
            RecipeScreen(navController, recipeId)
        }
        composable("add_recipe") {
            AddRecipeScreen(navController)
        }
    }
}
