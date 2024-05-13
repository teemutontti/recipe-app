package com.example.recipeapp.screens

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.example.recipeapp.Ingredient
import com.example.recipeapp.Recipe
import com.example.recipeapp.RecipeRepository
import com.example.recipeapp.composables.NavBar
import com.example.recipeapp.composables.RecipeButton

@Composable
fun RecipeScreen(uri: String?) {
    var loading by remember { mutableStateOf(true) }
    var recipe: Recipe? by remember { mutableStateOf(null) }

    val context = LocalContext.current
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)

    LaunchedEffect(key1 = uri) {
        val realUri: String? = uri?.replace("___", "/")?.replace("...", "#")
        if (realUri != null) {
            recipe = recipeViewModel.fetchRecipeByUri(context, realUri)
        }
        loading = false
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.padding(horizontal = 25.dp, vertical = 35.dp)) {
            Text(
                text = recipe?.label ?: "Loading...",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(50.dp))
            Column(horizontalAlignment = Alignment.Start) {
                if (loading) {
                    CircularProgressIndicator()
                } else if (recipe != null) {
                    Text("Ingredients")
                    Column {
                        recipe?.ingredients?.forEach {
                            Text(text = "$it")
                        }
                    }
                }
            }
        }
    }
}