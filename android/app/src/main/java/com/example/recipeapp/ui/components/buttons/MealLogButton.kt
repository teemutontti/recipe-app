package com.example.recipeapp.ui.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.example.recipeapp.models.FoodLog

@Composable
fun MealLogButton(foodLog: FoodLog) {
    val calories = (foodLog.food.calories * (foodLog.log.amount / 100)).toInt()
    val carbs = (foodLog.food.carbs * (foodLog.log.amount / 100)).toInt()
    val protein = (foodLog.food.protein * (foodLog.log.amount / 100)).toInt()
    val fat = (foodLog.food.fat * (foodLog.log.amount / 100)).toInt()

    val calorieString = "$calories kcal"
    val nutrientString = "${carbs}/${protein}/${fat}"

    TextButton(
        onClick = { /*TODO*/ },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(0.7f)) {
                Text(
                    text = foodLog.food.name,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Text(
                    text = "${foodLog.food.barcode}, ${foodLog.log.amount.toInt()} g",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
            Column(modifier = Modifier.weight(0.3f), horizontalAlignment = Alignment.End) {
                Text(
                    text = calorieString,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Clip,
                    maxLines = 1,
                )
                Text(
                    text = nutrientString,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}