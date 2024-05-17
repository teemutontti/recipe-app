package com.example.recipeapp.screens

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.composables.NavBar
import com.example.recipeapp.composables.RecipeButton
import com.example.recipeapp.repositories.RecipeRepository

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
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
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(recipeViewModel.specials) { index, item ->
                        Column(modifier = Modifier.width(296.dp)) {
                            Text(
                                text = when (index) {
                                    0 -> "Breakfast"
                                    1 -> "Lunch"
                                    2 -> "Dinner"
                                    else -> "Snack"
                                },
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            RecipeButton(
                                navController = navController,
                                recipe = item,
                                showAdditionalInfo = true
                            )
                        }
                        if (index < recipeViewModel.specials.size - 1) {
                            Spacer(modifier = Modifier.width(24.dp))
                        }
                    }
                }
                TextButton(onClick = { navController.navigate("add_recipe") }) {
                    Text("Add New Recipe")
                }
            }
        }
    }

}