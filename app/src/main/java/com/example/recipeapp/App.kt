package com.example.recipeapp

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.composables.NavBar
import com.example.recipeapp.screens.HomeScreen
import com.example.recipeapp.screens.MyRecipesScreen
import com.example.recipeapp.screens.RecipeScreen
import com.example.recipeapp.screens.SearchScreen

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("search") {
            SearchScreen(navController)
        }
        composable("my_recipes") {
            MyRecipesScreen(navController)
        }
        composable("recipe") {
            RecipeScreen(navController)
        }
        composable("recipe_fetch") {
            RecipeScreen(navController, fetchInfo = true)
        }
    }
}
