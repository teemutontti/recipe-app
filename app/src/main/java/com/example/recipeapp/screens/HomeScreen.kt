package com.example.recipeapp.screens

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.composables.NavBar
import com.example.recipeapp.composables.RecipeButton
import com.example.recipeapp.composables.TopBar
import com.example.recipeapp.repositories.RecipeRepository
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(title = "Recipe App") },
        content = {
            HomeScreenContent(navController = navController, paddingValues = it)
        },
        bottomBar = {
            NavBar(navController = navController, selected = "home")
        }
    )
}

@Composable
private fun HomeScreenContent(navController: NavController, paddingValues: PaddingValues) {
    val context = LocalContext.current
    var loading: Boolean by remember { mutableStateOf(true) }
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)

    LaunchedEffect(key1 = Unit) {
        delay(1000)
        recipeViewModel.fetchRandomRecipes(context)
        Log.d("HomeScreen", "${recipeViewModel.specials.size}")
        for (item in recipeViewModel.specials) {
            Log.d("HomeScreen", item.toString())
        }
        loading = false
    }

    Column(modifier = Modifier
        .padding(paddingValues)
        .verticalScroll(rememberScrollState())) {
        Column(modifier = Modifier.padding(24.dp)) {
            if (loading) LinearProgressIndicator()
            else {
                Text("Today's Specials:", style = TextStyle(fontSize = 24.sp))
                Spacer(modifier = Modifier.height(16.dp))
                recipeViewModel.specials.map {
                    RecipeButton(navController = navController, recipe = it)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

}