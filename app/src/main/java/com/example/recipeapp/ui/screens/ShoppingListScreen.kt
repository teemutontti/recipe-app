package com.example.recipeapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.models.ShoppingListItem
import com.example.recipeapp.ui.components.AddButton
import com.example.recipeapp.ui.components.NavBar
import com.example.recipeapp.ui.components.ShoppingListItemForm
import com.example.recipeapp.ui.components.ShoppingListItemRow
import com.example.recipeapp.ui.components.TopBar
import com.example.recipeapp.ui.components.UserFeedbackMessage
import com.example.recipeapp.viewmodels.ViewModelWrapper

@Composable
fun ShoppingListScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
) {
    Scaffold(
        topBar = {
            TopBar("Shopping List", subtitle = {
                ShoppingListItemForm(viewModels.shopping)
            })
         },
        content = { ShoppingListContent(navController, it, viewModels) },
        bottomBar = { NavBar(navController, "shopping_list") }
    )
}

@Composable
private fun ShoppingListContent(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModels: ViewModelWrapper,
) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.padding(paddingValues)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp)) {
                Text("Items", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                if (viewModels.shopping.items.isEmpty()) {
                    UserFeedbackMessage("Seems like you have an empty shopping list")
                }
                viewModels.shopping.items.forEachIndexed { index, item ->
                    ShoppingListItemRow(index, item, viewModels.shopping)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
