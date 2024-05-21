package com.example.recipeapp.screens

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.recipeapp.R
import com.example.recipeapp.api.Instructions
import com.example.recipeapp.api.Recipe
import com.example.recipeapp.composables.IngredientLine
import com.example.recipeapp.repositories.RecipeRepository
import kotlinx.coroutines.delay

@Composable
fun RecipeScreen(
    navController: NavController,
    recipeId: Int?
) {
    val context = LocalContext.current
    var showFavourite by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(true) }
    var isFavourite by remember { mutableStateOf(false) }
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)

    LaunchedEffect(Unit) {
        if (recipeId != null) {
            recipeViewModel.fetchRecipeById(context, recipeId)
            loading = false
        } else {
            loading = false
            showFavourite = false
        }

        val favouriteIds: List<Int> = recipeViewModel.favourites.map { it.id.toInt() }
        if (favouriteIds.contains(recipeId)) {
            isFavourite = true
        }
    }

    fun handleFavouriteClick() {
        if (recipeViewModel.selectedRecipe != null) {
            isFavourite = !isFavourite
            if (isFavourite) {
                recipeViewModel.addFavourite(context, recipeViewModel.selectedRecipe!!)
            } else {
                recipeViewModel.deleteFavourite(context, recipeViewModel.selectedRecipe!!)
            }
        }
    }

    Column(modifier = Modifier
        .padding(32.dp)
        .verticalScroll(rememberScrollState())
    ) {
        if (loading) LinearProgressIndicator()
        else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = recipeViewModel.selectedRecipe?.title ?: "Error",
                    overflow = TextOverflow.Visible,
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
                if (showFavourite) {
                    IconButton(onClick = { handleFavouriteClick() }) {
                        Icon(
                            imageVector =
                            if (isFavourite) Icons.Rounded.Favorite
                            else Icons.Rounded.FavoriteBorder,
                            contentDescription = "star icon",
                            modifier = Modifier
                                .width(32.dp)
                                .height(32.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (recipeViewModel.selectedRecipe?.image != null
                && recipeViewModel.selectedRecipe?.image != ""
            ) {
                AsyncImage(
                    model = recipeViewModel.selectedRecipe?.image,
                    contentDescription = "${recipeViewModel.selectedRecipe?.title} image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .aspectRatio(1.5f)
                        .fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.meal),
                    contentDescription = "meal",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .aspectRatio(1.5f)
                        .fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column {
                // TODO: Add functionality to change the serving size
                Text(text = "${recipeViewModel.selectedRecipe?.servings} servings")
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Ingredients",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    recipeViewModel.selectedRecipe?.extendedIngredients?.forEach {
                        IngredientLine(ingredient = it)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Instructions",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                recipeViewModel.selectedRecipe?.analyzedInstructions?.map { instruction ->
                    instruction.steps.map { step ->
                        Row {
                            Text(
                                text = "${step.number}",
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(24.dp),
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = step.step, modifier = Modifier.padding(top = 2.dp))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}