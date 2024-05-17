package com.example.recipeapp.screens

import android.content.Context
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.recipeapp.composables.CustomSearchBar
import com.example.recipeapp.composables.NavBar
import com.example.recipeapp.composables.RecipeButton
import com.example.recipeapp.composables.TopBar
import com.example.recipeapp.repositories.RecipeRepository

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
    val context = LocalContext.current
    var loading: Boolean by remember { mutableStateOf(true) }
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)



    Column(modifier = Modifier
        .padding(paddingValues)
        .verticalScroll(rememberScrollState())) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            CustomSearchBar(navController, recipeViewModel)
            Spacer(modifier = Modifier.height(24.dp))
            TodaysSpecialsSection(recipeViewModel, navController, context)
        }
    }

}

@Composable
fun TodaysSpecialsSection(
    recipeViewModel: RecipeRepository,
    navController: NavController,
    context: Context
) {
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = Unit) {
        recipeViewModel.fetchRandomRecipes(context)
        loading = false
    }

    if (loading) LinearProgressIndicator()
    else {
        Text(
            text = "Check out today's specials",
            style = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        )
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(recipeViewModel.specials) { index, item ->
                Column(modifier = Modifier.width(296.dp)) {
                    /* TODO: Move this inside the button
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
                    */
                    Spacer(modifier = Modifier.height(4.dp))
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
    }
}