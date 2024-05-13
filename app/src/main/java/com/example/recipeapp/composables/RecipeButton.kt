package com.example.recipeapp.composables

import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.recipeapp.api.Recipe
import com.example.recipeapp.repositories.RecipeRepository

@Composable
fun RecipeButton(navController: NavController, recipe: Recipe) {
    val context = LocalContext.current
    var loading by remember { mutableStateOf(true) }
    var imageError by remember { mutableStateOf(false) }
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)

    fun handleRecipeClick() {
        navController.navigate("recipe/${recipe.id}")
    }

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
        modifier = Modifier.padding(16.dp),
        onClick = { handleRecipeClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .padding(end = 10.dp, top = 10.dp, bottom = 10.dp)
                .clip(RoundedCornerShape(15.dp))
                .border(8.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(15.dp))
            ) {
                if (loading && !imageError) CircularProgressIndicator()
                if (imageError) Text("Image Error")
                AsyncImage(
                    model = recipe.image,
                    contentDescription = "${recipe.title} picture",
                    onSuccess = { (_) -> loading = false },
                    onError = { (_) -> imageError = true },
                    modifier = Modifier
                        .height(125.dp)
                        .width(125.dp)
                )
            }
            Text(
                text = recipe.title,
                style = TextStyle(
                    fontSize = 20.sp
                )
            )
        }
    }
}