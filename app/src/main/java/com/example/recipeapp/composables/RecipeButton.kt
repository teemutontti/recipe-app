package com.example.recipeapp.composables

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.recipeapp.api.Recipe
import com.example.recipeapp.repositories.RecipeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RecipeButton(navController: NavController, recipe: Recipe, fetchInfo: Boolean = false) {
    val context = LocalContext.current
    var loading by remember { mutableStateOf(true) }
    var imageError by remember { mutableStateOf(false) }
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)

    fun handleRecipeClick() {
        recipeViewModel.updateSelectedRecipe(recipe)
        if (fetchInfo) navController.navigate("recipe_fetch")
        else navController.navigate("recipe")
    }

    TextButton(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceVariant),
        onClick = { handleRecipeClick() },
        modifier = Modifier.padding(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(15.dp))
                .border(4.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(15.dp))
            ) {
                if (loading && !imageError) CircularProgressIndicator()
                if (imageError) Text("Image Error")
                AsyncImage(
                    model = recipe.image,
                    contentDescription = "${recipe.title} picture",
                    onSuccess = { (_) -> loading = false },
                    onError = { (_) -> imageError = true },
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 96.dp, height = 96.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            Text(
                text = recipe.title ?: "Loading...",
                style = TextStyle(
                    fontSize = 20.sp
                )
            )
        }
    }
}