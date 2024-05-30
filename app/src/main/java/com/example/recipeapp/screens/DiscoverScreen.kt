package com.example.recipeapp.screens

import android.content.Context
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.ApplicationContext
import com.example.recipeapp.composables.AddRecipeButton
import com.example.recipeapp.composables.CustomSearchBar
import com.example.recipeapp.composables.NavBar
import com.example.recipeapp.composables.RecipeShelf
import com.example.recipeapp.composables.SearchPanel
import com.example.recipeapp.composables.TopBar
import com.example.recipeapp.models.SharedPreferencesKeys.PREFS_NAME
import com.example.recipeapp.models.room.AppDatabase
import com.example.recipeapp.viewmodels.PersonalRecipesViewModel
import com.example.recipeapp.viewmodels.RecipeUnderInspectionViewModel
import com.example.recipeapp.viewmodels.TodaysSpecialsViewModel
import com.example.recipeapp.viewmodels.ViewModelWrapper

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

@Composable
private fun DiscoverScreenContent(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModels: ViewModelWrapper,
) {
    var showSearchPanel by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.fillMaxSize().padding(paddingValues)
    ) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                /* === SEARCH SECTION === */
                CustomSearchBar(viewModels.search) {
                    showSearchPanel = it
                }
                if (showSearchPanel) SearchPanel(navController, viewModels)
                Spacer(modifier = Modifier.height(16.dp))

                /* === TODAY'S SPECIALS SECTION === */
                if (viewModels.specials.loading) {
                    LinearProgressIndicator()
                } else {
                    Text("Today's Specials", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    RecipeShelf(
                        navController,
                        viewModels.specials.recipes,
                        viewModels,
                        animate = true,
                    )
                }
            }
        }
        AddRecipeButton(navController, viewModels.inspection)
    }
}
