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
import androidx.room.Room
import com.example.recipeapp.ApplicationContext
import com.example.recipeapp.composables.AddRecipeButton
import com.example.recipeapp.composables.CustomSearchBar
import com.example.recipeapp.composables.NavBar
import com.example.recipeapp.composables.RecipeShelf
import com.example.recipeapp.composables.SearchPanel
import com.example.recipeapp.composables.TopBar
import com.example.recipeapp.models.SharedPreferencesKeys.PREFS_NAME
import com.example.recipeapp.models.room.AppDatabase
import com.example.recipeapp.viewmodels.TodaysSpecialsViewModel

@Composable
fun DiscoverScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(title = "Discover") },
        content = {
            DiscoverScreenContent(navController = navController, paddingValues = it)
        },
        bottomBar = {
            NavBar(navController = navController, selected = "discover")
        }
    )
}

@Composable
private fun DiscoverScreenContent(navController: NavController, paddingValues: PaddingValues) {
    val todaysSpecialsViewModel: TodaysSpecialsViewModel = viewModel()

    var showSearchPanel by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                CustomSearchBar(
                    onSearchAction = { showSearchPanel = true },
                    onBackAction = { showSearchPanel = false }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxSize()
            ) {
                if (todaysSpecialsViewModel.loading) {
                    LinearProgressIndicator()
                } else {
                    Column {
                        Text(
                            text = "Today's Specials",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        RecipeShelf(navController, todaysSpecialsViewModel.recipes)
                    }
                }
                if (showSearchPanel) {
                    SearchPanel(navController = navController)
                }
            }
        }
        AddRecipeButton(navController)
    }
}