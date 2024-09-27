package com.example.recipeapp.ui.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddShoppingCart
import androidx.compose.material.icons.rounded.AddTask
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.recipeapp.models.Ingredient
import com.example.recipeapp.models.ShoppingListItem
import com.example.recipeapp.utils.Utils.convertToFraction
import com.example.recipeapp.viewmodels.ViewModelWrapper
import kotlinx.coroutines.delay

/**
 * A composable function that displays a row representing an ingredient.
 * It shows the ingredient's amount, unit, and name, and optionally includes a delete button.
 *
 * @param index The index of the ingredient in the list.
 * @param ingredient The ingredient to display.
 * @param handleDelete An optional lambda function to handle the deletion of the ingredient.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientRow(
    index: Int,
    ingredient: Ingredient,
    viewModels: ViewModelWrapper? = null,
    handleDelete: ((Int) -> Unit)? = null,
) {
    val amountWithFraction: String = convertToFraction(ingredient.amount)
    var showSwipe by remember { mutableStateOf(true) }
    val swipeState = rememberDismissState()
    var iconSize by remember { mutableFloatStateOf(1f) }

    if (viewModels != null) {
        LaunchedEffect(swipeState.currentValue) {
            Log.d("Ingredient", "${swipeState.currentValue}")
            if (swipeState.currentValue == DismissValue.DismissedToStart
                || swipeState.currentValue == DismissValue.DismissedToEnd) {
                iconSize = 16f
                swipeState.reset()
                viewModels.shopping.add(
                    ShoppingListItem(
                        name = ingredient.name,
                        note = "${ingredient.amount} ${ingredient.unit}"
                    )
                )
            }
        }

        LaunchedEffect(swipeState.progress) {
            iconSize += swipeState.progress * 15
        }
    }

    AnimatedVisibility(showSwipe, exit = fadeOut(spring())) {
        SwipeToDismiss(
            state = swipeState,
            background = { SwipeBackgroundElement(swipeState, Icons.Rounded.AddShoppingCart) },
            directions = if (viewModels != null) setOf(DismissDirection.StartToEnd) else setOf(),
            dismissContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Text(
                        text = "$amountWithFraction ${ingredient.unit}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .width(104.dp)
                            .padding(horizontal = 8.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = ingredient.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                    )
                    if (handleDelete != null) {
                        RemoveButton(index, handleDelete)
                    }
                }
            },
        )
    }
}
