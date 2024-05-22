package com.example.recipeapp.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.recipeapp.api.Ingredient
import com.example.recipeapp.composables.NumberCounter
import com.example.recipeapp.repositories.RecipeRepository
import com.example.recipeapp.utils.Utils

@Composable
fun RecipeScreen(
    navController: NavController,
    apiRecipeId: Int? = null,
    preview: Boolean = false
) {
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)
    val context = LocalContext.current

    // State declarations
    var loading by remember { mutableStateOf(true) }
    var isFavourite by remember { mutableStateOf(false) }
    var showMore by remember { mutableStateOf(false) }
    var ingredients = remember { mutableStateListOf<Ingredient>() }

    LaunchedEffect(Unit) {
        if (apiRecipeId != null) {
            // Fetching recipe from API
            recipeViewModel.fetchRecipeById(context, apiRecipeId)

            // Checking if the recipe is a favourite and updating state based on it
            if (recipeViewModel.favourites.map { it.id.toInt() }.contains(apiRecipeId)) {
                isFavourite = true
            }
        }
        // Setting ingredients state so that servings size can be modified
        recipeViewModel.selectedRecipe?.extendedIngredients?.forEach {
            ingredients.add(it)
        }
        loading = false
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

    fun calculateIngredients(newServings: Int) {
        val ingredientsPerServing = recipeViewModel.selectedRecipe?.extendedIngredients?.map {
            it.measures.metric.amount.toFloat() / recipeViewModel.selectedRecipe!!.servings.toInt()
        }

        val newIngredientAmounts = ingredientsPerServing?.map { it * newServings }

        val newIngredients = recipeViewModel.selectedRecipe?.extendedIngredients?.mapIndexed { index: Int, item: Ingredient ->
            item.copy(
                measures = item.measures.copy(
                    metric = item.measures.metric.copy(
                        amount = newIngredientAmounts?.get(index) ?: -1
                    )
                )
            )
        }

        if (newIngredients != null) {
            ingredients.clear()
            newIngredients.map {
                ingredients.add(it)
            }
        }
    }

    Column(modifier =
        if (preview) Modifier.padding(32.dp)
        else Modifier
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
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.weight(1f)
                )
                if (apiRecipeId != null) {
                    IconButton(onClick = { handleFavouriteClick() }) {
                        Icon(
                            imageVector =
                            if (isFavourite) Icons.Rounded.Star
                            else Icons.Rounded.StarBorder,
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
                if (recipeViewModel.selectedRecipe?.servings != null) {
                    NumberCounter(
                        value = recipeViewModel.selectedRecipe?.servings!!.toInt(),
                        suffix = "servings",
                        onNumberChange = ::calculateIngredients,
                        max = 20
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Ingredients",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    ingredients.forEach {
                        IngredientRow(it)
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

@Composable
fun IngredientRow(ingredient: Ingredient) {
    val fractionStr = Utils.convertToFraction(ingredient.measures.metric.amount.toFloat())

    Row(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.width(104.dp)
        ) {
            if (fractionStr != null) {
                if (ingredient.measures.metric.amount.toInt() != 0) {
                    Text(text = "${ingredient.measures.metric.amount.toInt()}")
                    Spacer(modifier = Modifier.width(4.dp))
                }
            } else {
                Text(text = Utils.formatFloatToString(ingredient.measures.metric.amount.toFloat()))
                Spacer(modifier = Modifier.width(4.dp))
            }
            if (fractionStr != null) {
                Text(
                    text = fractionStr,
                    style = TextStyle(fontSize = 12.sp),
                    modifier = Modifier.padding(bottom = 1.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(ingredient.measures.metric.unitShort)
        }
        Text(
            text = ingredient.name,
            modifier = Modifier.weight(1f),
            style = TextStyle(lineHeight = 16.sp, fontSize = 16.sp)
        )
    }
    Spacer(modifier = Modifier.height(6.dp))
}