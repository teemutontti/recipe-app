package com.example.recipeapp.ui.screens.food

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.recipeapp.ui.components.inputs.FoodForm
import com.example.recipeapp.viewmodels.ViewModelWrapper

@Composable
internal fun ViewMode(navController: NavController, viewModels: ViewModelWrapper) {
    Column(modifier = Modifier.padding(horizontal = 40.dp)) {
        Text("Add food", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))
        FoodForm(navController, viewModels)
    }
}