package com.example.recipeapp.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.recipeapp.ui.screens.food.AddFoodScreen
import com.example.recipeapp.viewmodels.AuthViewModel
import com.example.recipeapp.viewmodels.FavouriteRecipesViewModel
import com.example.recipeapp.viewmodels.LogsScreenViewModel
import com.example.recipeapp.viewmodels.PersonalRecipesViewModel
import com.example.recipeapp.viewmodels.RecipeUnderInspectionViewModel
import com.example.recipeapp.viewmodels.SearchViewModel
import com.example.recipeapp.viewmodels.ShoppingListViewModel
import com.example.recipeapp.viewmodels.TodaysSpecialsViewModel
import com.example.recipeapp.viewmodels.ViewModelWrapper
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddFoodScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

    val viewModels = ViewModelWrapper(
        favourite = FavouriteRecipesViewModel(ApplicationProvider.getApplicationContext()),
        personal = PersonalRecipesViewModel(ApplicationProvider.getApplicationContext()),
        inspection = RecipeUnderInspectionViewModel(ApplicationProvider.getApplicationContext()),
        search = SearchViewModel(ApplicationProvider.getApplicationContext()),
        specials = TodaysSpecialsViewModel(ApplicationProvider.getApplicationContext()),
        shopping = ShoppingListViewModel(ApplicationProvider.getApplicationContext()),
        logsScreen = LogsScreenViewModel(ApplicationProvider.getApplicationContext()),
        authViewModel = AuthViewModel(ApplicationProvider.getApplicationContext()),
    )

    @Test
    fun testComposable_showsScreen() {
        composeTestRule.setContent { 
            AddFoodScreen(navController = navController, viewModels = viewModels)
        }

        composeTestRule.onNodeWithText("Add food").assertIsDisplayed()
    }
    
}