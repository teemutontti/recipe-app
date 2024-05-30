package com.example.recipeapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.ui.components.AddRecipeButton
import com.example.recipeapp.ui.components.CategoryShelf
import com.example.recipeapp.ui.components.CustomSearchBar
import com.example.recipeapp.ui.components.NavBar
import com.example.recipeapp.ui.components.RecipeShelf
import com.example.recipeapp.ui.components.SearchPanel
import com.example.recipeapp.ui.components.TopBar
import com.example.recipeapp.ui.components.UserFeedbackMessage
import com.example.recipeapp.viewmodels.ViewModelWrapper

/**
 * Composable function for displaying the Discover screen.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
fun DiscoverScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
) {
    Scaffold(
        topBar = { TopBar(title = "Discover") },
        content = { DiscoverScreenContent(navController, it, viewModels) },
        bottomBar = { NavBar(navController, "discover") }
    )
}

/**
 * Composable function for the content of the Discover screen.
 * @param navController The navigation controller for navigating between screens.
 * @param paddingValues Padding values for the content.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
private fun DiscoverScreenContent(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModels: ViewModelWrapper,
) {
    var showSearchPanel by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                /* === SEARCH SECTION === */
                CustomSearchBar(viewModels.search, showSearchPanel) { showSearchPanel = it }
                if (showSearchPanel) SearchPanel(navController, viewModels)
                Spacer(modifier = Modifier.height(16.dp))

                /* === TODAY'S SPECIALS SECTION === */
                Text("Today's Specials", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                if (viewModels.specials.error != null) {
                    UserFeedbackMessage(viewModels.specials.error!!, "error")
                } else if (viewModels.specials.loading) {
                    LinearProgressIndicator()
                } else {
                    RecipeShelf(
                        navController,
                        viewModels.specials.recipes,
                        viewModels,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                /* === CATEGORY SEARCH SECTION === */
                Text("Categories", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                CategoryShelf(viewModels) { showSearchPanel = it }
            }
        }
        AddRecipeButton(navController, viewModels.inspection)
    }
}
