package com.example.recipeapp.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.composables.CustomSearchBar
import com.example.recipeapp.composables.RecipeList
import com.example.recipeapp.composables.TopBar
import com.example.recipeapp.repositories.RecipeRepository

@Composable
fun SearchResultsScreen(navController: NavController, query: String?) {
    Scaffold(
        topBar = { TopBar(title = "Search Results") },
        content = {
            SearchResultsScreenContent(
                navController = navController,
                paddingValues = it,
                query = query
            )
        },
    )
}

@Composable
fun SearchResultsScreenContent(
    navController: NavController,
    paddingValues: PaddingValues,
    query: String?
) {
    val context = LocalContext.current
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)

    Column(modifier = Modifier.padding(paddingValues)) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            CustomSearchBar(
                navController = navController,
                recipeViewModel = recipeViewModel,
                incomingQuery = query,
                inSearchResultsScreen = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                RecipeList(navController = navController, recipes = recipeViewModel.searchResults)
            }
        }
    }
}
