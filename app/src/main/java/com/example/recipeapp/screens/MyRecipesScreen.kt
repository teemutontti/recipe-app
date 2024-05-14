package com.example.recipeapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import com.example.recipeapp.composables.NavBar
import com.example.recipeapp.composables.TopBar
import kotlinx.coroutines.delay

@Composable
fun MyRecipesScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(title = "Recipe App") },
        content = {
            MyRecipesScreenContent(navController = navController, paddingValues = it)
        },
        bottomBar = {
            NavBar(navController = navController, selected = "my_recipes")
        }
    )
}

@Composable
fun MyRecipesScreenContent(navController: NavController, paddingValues: PaddingValues) {
    var loading: Boolean by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1000)
        loading = false
    }

    Column(modifier = Modifier.padding(paddingValues)) {
        Column(modifier = Modifier.padding(24.dp)) {
            if (loading) LinearProgressIndicator()
            else {
                Text("My Recipes screen content")
            }
        }
    }
}