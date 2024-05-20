package com.example.recipeapp.composables

import android.graphics.ColorSpace.Rgb
import android.hardware.SensorAdditionalInfo
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.recipeapp.R
import com.example.recipeapp.api.CachedRecipe
import com.example.recipeapp.api.Recipe
import com.example.recipeapp.repositories.RecipeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TodaysSpecialButton(
    navController: NavController,
    recipe: CachedRecipe?,
    index: Int
) {
    var loading by remember { mutableStateOf(true) }
    var imageError by remember { mutableStateOf(false) }

    fun handleRecipeClick() {
        if (recipe != null) navController.navigate("recipe/${recipe.id}")
    }

    if (recipe != null) {
        TextButton(
            shape = RoundedCornerShape(8.dp),
            onClick = { handleRecipeClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .fillMaxSize()
                    .height(162.dp)
                    .border(2.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(15.dp))
            ) {
                if (loading && !imageError) CircularProgressIndicator()
                if (!imageError) {
                    AsyncImage(
                        model = recipe.image,
                        contentDescription = "${recipe.title} picture",
                        onSuccess = { (_) -> loading = false },
                        onError = { (_) -> imageError = true },
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(width = 288.dp, height = 162.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .fillMaxWidth()
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.meal),
                        contentDescription = "meal",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(width = 288.dp, height = 162.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .fillMaxSize()
                    )
                }
                // Adding shadow with box composable for better text readability
                Box(
                    modifier = Modifier.fillMaxSize().background(Brush.linearGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.7f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f),
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(0f, Float.POSITIVE_INFINITY)
                    ))
                )
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = when (index) {
                            0 -> "Breakfast"
                            1 -> "Lunch"
                            2 -> "Dinner"
                            else -> "Snack"
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = recipe.title ?: "Loading...",
                        style = TextStyle(
                            fontSize = 20.sp
                        )
                    )
                }
            }
        }
    }
}