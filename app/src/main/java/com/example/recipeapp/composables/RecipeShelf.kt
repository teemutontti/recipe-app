package com.example.recipeapp.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun RecipeShelf(items: List<Any>) {
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        itemsIndexed(items) { index, item ->
            Column(modifier = Modifier.width(296.dp)) {
                Spacer(modifier = Modifier.height(4.dp))
                TodaysSpecialButton(navController = navController, recipe = item, index = index)
            }
            if (index < recipeViewModel.specials.size - 1) {
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(times = recipeViewModel.specials.size) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (it == specialInView) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
            )
        }
    }
}