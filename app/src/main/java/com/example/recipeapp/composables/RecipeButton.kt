package com.example.recipeapp.composables

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.ApplicationContext
import com.example.recipeapp.R
import com.example.recipeapp.models.CachedRecipe
import com.example.recipeapp.models.SharedPreferencesKeys.PREFS_NAME
import com.example.recipeapp.models.room.FavouriteRecipe
import com.example.recipeapp.models.room.PersonalRecipe
import com.example.recipeapp.viewmodels.RecipeUnderInspectionViewModel

@Composable
fun RecipeButton(
    navController: NavController,
    apiRecipe: CachedRecipe? = null,
    ownRecipe: PersonalRecipe? = null,
) {
    val recipeUnderInspectionViewModel: RecipeUnderInspectionViewModel = viewModel()

    var loading by remember { mutableStateOf(true) }
    var imageError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        Log.d("RecipeButton", "apiRecipe: $apiRecipe")
        Log.d("RecipeButton", "ownRecipe: $ownRecipe")
    }

    fun handleRecipeClick() {
        if (apiRecipe != null) {
            navController.navigate("recipe/${apiRecipe.id}")
        } else if (ownRecipe != null) {
            recipeUnderInspectionViewModel.setRecipe(ownRecipe)
            navController.navigate("recipe/${null}")
        }
    }

    TextButton(
        shape = RoundedCornerShape(8.dp),
        onClick = { handleRecipeClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.scrim
        )
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .fillMaxSize()
                .height(96.dp)
                .border(2.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(15.dp))
        ) {
            if (!imageError) {
                RecipeImage(
                    model = apiRecipe?.image ?: ownRecipe?.image,
                    isPreview = true,
                    onLoadError = { imageError = true },
                    onLoadSuccess = {
                        imageError = false
                        loading = false
                    }
                )
            } else {
                RecipeImage(
                    painter = painterResource(id = R.drawable.meal),
                    isPreview = true
                )
            }
            // Adding shadow with box composable for better text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f),
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(0f, Float.POSITIVE_INFINITY)
                        )
                    )
            )
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = apiRecipe?.title ?: ownRecipe?.title ?: "Loading...",
                    style = TextStyle(
                        fontSize = 20.sp
                    )
                )
            }
            if (loading && !imageError) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}