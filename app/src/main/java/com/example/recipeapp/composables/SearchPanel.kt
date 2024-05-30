package com.example.recipeapp.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.viewmodels.SearchViewModel
import com.example.recipeapp.viewmodels.ViewModelWrapper

@Composable
fun SearchPanel(navController: NavController, viewModels: ViewModelWrapper) {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface).fillMaxSize()) {
        if (viewModels.search.loading) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(8.dp)
            ) {
                CircularProgressIndicator()
            }
        } else {
            RecipeList(navController, viewModels.search.searchResults, viewModels)
        }
    }
}
