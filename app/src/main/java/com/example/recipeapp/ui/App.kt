package com.example.recipeapp.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.utils.LocalApplicationContext
import com.example.recipeapp.ui.screens.RecipeEditorScreen
import com.example.recipeapp.ui.screens.DiscoverScreen
import com.example.recipeapp.ui.screens.CookbookScreen
import com.example.recipeapp.ui.screens.RecipeScreen
import com.example.recipeapp.viewmodels.FavouriteRecipesViewModel
import com.example.recipeapp.viewmodels.PersonalRecipesViewModel
import com.example.recipeapp.viewmodels.RecipeUnderInspectionViewModel
import com.example.recipeapp.viewmodels.SearchViewModel
import com.example.recipeapp.viewmodels.TodaysSpecialsViewModel
import com.example.recipeapp.viewmodels.ViewModelWrapper

/**
 * Composable function representing the entry point of the Recipe App.
 *
 * This function sets up navigation using Jetpack Compose's NavHost and defines
 * different screens of the app such as DiscoverScreen, CookbookScreen, RecipeScreen,
 * and RecipeEditorScreen.
 *
 * @param applicationContext The application context required for providing dependencies.
 */
@Composable
fun App(applicationContext: Context) {
    val navController = rememberNavController()

    val inspectionViewModel: RecipeUnderInspectionViewModel = viewModel()
    val personalRecipesViewModel: PersonalRecipesViewModel = viewModel()
    val favouriteRecipesViewModel: FavouriteRecipesViewModel = viewModel()
    val searchViewModel: SearchViewModel = viewModel()
    val todaysSpecialsViewModel: TodaysSpecialsViewModel = viewModel()

    val viewModels = ViewModelWrapper(
        favourite = favouriteRecipesViewModel,
        personal = personalRecipesViewModel,
        inspection = inspectionViewModel,
        search = searchViewModel,
        specials = todaysSpecialsViewModel,
    )

    CompositionLocalProvider(LocalApplicationContext provides applicationContext) {
        NavHost(navController = navController, startDestination = "discover") {
            composable("discover") {
                DiscoverScreen(navController, viewModels)
            }
            composable("cookbook") {
                CookbookScreen(navController, viewModels)
            }
            composable("recipe") {
                RecipeScreen(navController, viewModels)
            }
            composable("recipe_editor") {
                RecipeEditorScreen(navController, viewModels)
            }
        }
    }
}
