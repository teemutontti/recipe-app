package com.example.recipeapp.screens

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.composables.NavBar
import com.example.recipeapp.composables.RecipeButton
import com.example.recipeapp.composables.TopBar
import com.example.recipeapp.repositories.RecipeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(title = "Recipe App") },
        content = {
            SearchScreenContent(navController = navController, paddingValues = it)
        },
        bottomBar = {
            NavBar(navController = navController, selected = "search")
        }
    )
}

@Composable
fun SearchScreenContent(navController: NavController, paddingValues: PaddingValues) {
    var loading by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    val context: Context = LocalContext.current

    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)

    fun handleSearch() {
        Log.d("Search", query)
        loading = true
        CoroutineScope(Dispatchers.Default).launch {
            recipeViewModel.searchRecipes(context, query)
            loading = false
        }
    }

    Column(modifier = Modifier.padding(paddingValues)) {
        Column(modifier = Modifier
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
        ) {
            TextField(
                value = query,
                onValueChange = { query = it },
                // TODO: Add search format instructions
                //supportingText = { Text("Test") },
                placeholder = { Text("Search...") },
                leadingIcon = { Icon(Icons.Rounded.Search, "search icon") },
                singleLine = true,
                keyboardActions = KeyboardActions(onSearch = { handleSearch() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            )
            Spacer(modifier = Modifier.height(32.dp))
            Column {
                if (loading) LinearProgressIndicator()
                else {
                    recipeViewModel.searchResults.map {
                        RecipeButton(navController = navController, recipe = it, fetchInfo = true)
                    }
                }
            }
        }
    }
}
