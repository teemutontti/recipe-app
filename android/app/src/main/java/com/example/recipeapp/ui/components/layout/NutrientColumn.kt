package com.example.recipeapp.ui.components.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NutrientColumn(nutrient: String, value: Double, suffix: String = "g") {
    Column {
        Text(
            text = nutrient,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
        )
        Text(text = "${value.toInt()} $suffix", style = MaterialTheme.typography.headlineMedium)
    }
}
