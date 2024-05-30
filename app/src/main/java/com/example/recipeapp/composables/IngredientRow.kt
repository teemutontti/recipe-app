package com.example.recipeapp.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.recipeapp.models.Ingredient
import com.example.recipeapp.utils.Utils.convertToFraction
import com.example.recipeapp.utils.Utils.DEFAULT_TEXT_STYLE

@Composable
fun IngredientRow(
    index: Int,
    ingredient: Ingredient,
    handleDelete: ((Int) -> Unit)? = null,
) {
    val amountWithFraction: String = convertToFraction(ingredient.amount)

    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$amountWithFraction ${ingredient.unit}",
            modifier = Modifier.width(104.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = ingredient.name,
            modifier = Modifier.weight(1f),
        )
        if (handleDelete != null) {
            RemoveButton(index, handleDelete)
        }
    }
}
