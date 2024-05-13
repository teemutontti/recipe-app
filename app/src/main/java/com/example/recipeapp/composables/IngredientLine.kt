package com.example.recipeapp.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipeapp.api.Ingredient

@Composable
fun IngredientLine(ingredient: Ingredient) {
    Row {
        Text(text = "${ingredient.measures.metric.amount} ${ingredient.measures.metric.unitShort} of ${ingredient.name}")
        Spacer(modifier = Modifier.width(6.dp))

        // TODO: Show weight of the ingredient?
        //Text(text = "${ingredient.weight}")
    }
}