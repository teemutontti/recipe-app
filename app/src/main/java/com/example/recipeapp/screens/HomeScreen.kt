package com.example.recipeapp.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.RecipeRepository
import com.example.recipeapp.composables.NavBar

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    var loading: Boolean by remember { mutableStateOf(true) }
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)

    LaunchedEffect(key1 = Unit) {
        delay(1000)
        RecipeRepository.fetchRandomRecipes(context)
        loading = false
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.padding(horizontal = 25.dp, vertical = 35.dp)) {
            Column {
                Text(
                    text = "Recipe App",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Home Screen",
                    style = TextStyle(
                        fontSize = 20.sp,
                    )
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
            Column(horizontalAlignment = Alignment.Start) {
                Text("Daily Random Recipes", textAlign = TextAlign.Left)
                if (loading) {
                    CircularProgressIndicator()
                } else {
                    LazyColumn {
                        items(RecipeRepository.recipes) {
                            TextButton(onClick = { /*TODO*/ }) {
                                Text(
                                    text = it.label,
                                    style = TextStyle(
                                        fontSize = 20.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        NavBar(navController = navController, selected = "home")
    }
}