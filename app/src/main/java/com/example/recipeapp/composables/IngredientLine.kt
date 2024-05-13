package com.example.recipeapp.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipeapp.Ingredient

@Composable
fun IngredientLine(ingredient: Ingredient) {
    Row {
        if (ingredient.quantity != "0.0" && ingredient.quantity != null) {
            Text(text = ingredient.quantity)
            Spacer(modifier = Modifier.width(6.dp))
        }
        if (ingredient.measure != "<unit>" && ingredient.measure != null) {
            Text(text = "${ingredient.measure} of")
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(text = ingredient.food ?: "")
        // TODO: Show weight of the ingredient?
        //Text(text = "${ingredient.weight}")
    }
}