package com.example.recipeapp.composables

import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.models.Recipe
import com.example.recipeapp.utils.Utils
import com.example.recipeapp.viewmodels.RecipeUnderInspectionViewModel
import com.example.recipeapp.viewmodels.TodaysSpecialsViewModel
import com.example.recipeapp.viewmodels.ViewModelWrapper
import kotlinx.coroutines.delay

@Composable
fun RecipeShelf(navController: NavController, recipes: List<Recipe>, viewModels: ViewModelWrapper) {
    var specialInView by remember { mutableIntStateOf(0) }
    val lazyRowState = rememberLazyListState()

    LaunchedEffect(key1 = specialInView) {
        lazyRowState.animateScrollToItem(specialInView)
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            val newSpecialInView = specialInView + 1
            if (newSpecialInView in 0..3) {
                specialInView = newSpecialInView
            } else {
                specialInView = 0
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LazyRow(
            state = lazyRowState,
            modifier = Modifier.width(Utils.IMAGE_WIDTH.dp)
        ) {
            itemsIndexed(recipes) { index, recipe ->
                Column(modifier = Modifier.fillParentMaxWidth()) {
                    TodaysSpecialButton(navController, index, recipe, viewModels.inspection)
                }
                if (index < recipes.size - 1) Spacer(modifier = Modifier.width(32.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
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
}