package com.example.recipeapp.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.utils.CachedRecipe

@Composable
fun RecipeShelf(navController: NavController, recipes: List<CachedRecipe>) {
    var specialInView by remember { mutableIntStateOf(0) }
    var lazyRowState = rememberLazyListState()

    LaunchedEffect(key1 = specialInView) {
        lazyRowState.animateScrollToItem(specialInView)
    }

    LazyRow(
        state = lazyRowState,
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(recipes) { index, recipe ->
            Column(modifier = Modifier.fillParentMaxWidth()) {
                Spacer(modifier = Modifier.height(4.dp))
                TodaysSpecialButton(
                    navController = navController,
                    recipe = recipe,
                    index = index
                )
            }
            if (index < recipes.size - 1) Spacer(modifier = Modifier.width(24.dp))
        }
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(times = recipes.size) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .clickable { specialInView = it }
                    .background(
                        if (lazyRowState.firstVisibleItemIndex == it) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
            )
        }
    }
}