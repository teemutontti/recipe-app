package com.example.recipeapp.screens

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.Room
import com.example.recipeapp.ApplicationContext
import com.example.recipeapp.R
import com.example.recipeapp.composables.DeleteDialog
import com.example.recipeapp.composables.NumberCounter
import com.example.recipeapp.composables.RecipeImage
import com.example.recipeapp.composables.UserFeedbackMessage
import com.example.recipeapp.models.SharedPreferencesKeys.PREFS_NAME
import com.example.recipeapp.models.room.AppDatabase
import com.example.recipeapp.models.room.DatabaseProvider
import com.example.recipeapp.models.room.PersonalIngredient
import com.example.recipeapp.models.room.PersonalRecipe
import com.example.recipeapp.repositories.RecipeRepository
import com.example.recipeapp.utils.Utils
import com.example.recipeapp.viewmodels.FavouriteRecipesViewModel
import com.example.recipeapp.viewmodels.RecipeUnderInspectionViewModel

@Composable
fun RecipeScreen(
    navController: NavController,
    apiRecipeId: Int? = null,
    preview: Boolean = false
) {
    val recipeUnderInspectionViewModel: RecipeUnderInspectionViewModel = viewModel()
    val favouriteRecipesViewModel: FavouriteRecipesViewModel = viewModel()

    // State declarations
    var imageLoading by remember { mutableStateOf(true) }
    var isFavourite by remember { mutableStateOf(false) }
    var showMore by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val ingredients = remember { mutableStateListOf<PersonalIngredient>() }
    var showServingsChangedNotice by remember { mutableStateOf(false) }

    val titleStyle = SpanStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
    val subtitleStyle = SpanStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Gray,
    )

    LaunchedEffect(Unit) {
        if (apiRecipeId != null) {
            // Fetching recipe from API
            recipeUnderInspectionViewModel.fetch(apiRecipeId)

            // Checking if the recipe is a favourite and updating state based on it
            val recipeIds = favouriteRecipesViewModel.recipes.map { it.id }
            isFavourite = recipeIds.contains(apiRecipeId)
        } else {
            recipeUnderInspectionViewModel.setLoadingDone()
        }

        // Setting ingredients state so that servings size can be modified
        ingredients.addAll(recipeUnderInspectionViewModel.recipe.ingredients)
    }

    fun handleFavouriteClick() {
        isFavourite = !isFavourite
        val recipe = recipeUnderInspectionViewModel.recipe
        if (isFavourite) {
            favouriteRecipesViewModel.add(recipe.toFavouriteRecipe())
        } else {
            favouriteRecipesViewModel.delete(recipe.toFavouriteRecipe())
        }
    }

    fun handleEditClick() {
        navController.navigate("recipe_editor")
    }

    fun handleDeleteClick() {
        showDeleteDialog = true
    }

    fun calculateIngredients(newServings: Int) {
        val recipe = recipeUnderInspectionViewModel.recipe
        showServingsChangedNotice = newServings != recipe.servings

        val ingredientsPerServing = recipe.ingredients.map {
            it.amount / recipe.servings
        }

        val newIngredientAmounts = ingredientsPerServing.map {
            it * newServings
        }

        val newIngredients = recipe.ingredients.mapIndexed { index: Int, item: PersonalIngredient ->
            item.copy(amount = newIngredientAmounts[index])
        }

        ingredients.clear()
        ingredients.addAll(newIngredients)
    }

    Column(modifier =
        if (preview) Modifier.padding(32.dp)
        else Modifier
            .padding(32.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (recipeUnderInspectionViewModel.loading) LinearProgressIndicator()
        else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    overflow = TextOverflow.Visible,
                    modifier = Modifier.weight(1f),
                    style = TextStyle(lineHeight = 32.sp),
                    // Building annotated string for heading text with different styles
                    text = buildAnnotatedString {
                        withStyle(style = titleStyle) {
                            append(recipeUnderInspectionViewModel.recipe.title)
                        }
                        withStyle(style = subtitleStyle) {
                            append(" #${recipeUnderInspectionViewModel.recipe.id}")
                        }
                    },
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
                } else {
                    Column {
                        IconButton(onClick = { showMore = !showMore }) {
                            Icon(Icons.Default.MoreVert, "more")
                        }
                        DropdownMenu(expanded = showMore, onDismissRequest = { showMore = false }) {
                            DropdownMenuItem(
                                modifier = Modifier.height(44.dp),
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Rounded.Edit,
                                            contentDescription = "edit",
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "Edit")
                                    }
                                },
                                onClick = { handleEditClick() }
                            )
                            DropdownMenuItem(
                                modifier = Modifier.height(44.dp),
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Delete,
                                            contentDescription = "delete",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Delete",
                                            style = TextStyle(color = MaterialTheme.colorScheme.error)
                                        )
                                    }
                                },
                                onClick = { handleDeleteClick() }
                            )
                        }
                        if (showDeleteDialog) {
                            DeleteDialog(navController = navController) { showDeleteDialog = false }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (recipeUnderInspectionViewModel.recipe.image != "") {
                if (imageLoading) CircularProgressIndicator()
                RecipeImage(
                    model = recipeUnderInspectionViewModel.recipe.image,
                    onLoadSuccess = { imageLoading = false }
                )
            } else {
                RecipeImage(painter = painterResource(id = R.drawable.meal))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column {
                // TODO: Add functionality to change the serving size
                NumberCounter(
                    value = recipeUnderInspectionViewModel.recipe.servings,
                    suffix = "servings",
                    onNumberChange = ::calculateIngredients,
                )
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
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "Instructions",
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                    )
                    if (showServingsChangedNotice) {
                        Spacer(modifier = Modifier.width(8.dp))
                        UserFeedbackMessage("Serving size changed", type = "warning")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                recipeUnderInspectionViewModel.recipe.instructions.map { instruction ->
                    Row {
                        Text(
                            text = "${instruction.number}",
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
                        Text(text = instruction.step, modifier = Modifier.padding(top = 2.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun IngredientRow(ingredient: PersonalIngredient) {
    val fractionStr = Utils.convertToFraction(ingredient.amount)

    Row(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.width(104.dp)
        ) {
            if (fractionStr != null) {
                if (ingredient.amount.toInt() != 0) {
                    Text(text = "${ingredient.amount.toInt()}")
                    Spacer(modifier = Modifier.width(4.dp))
                }
            } else {
                Text(text = Utils.formatFloatToString(ingredient.amount))
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
            Text(ingredient.unit)
        }
        Text(
            text = ingredient.name,
            modifier = Modifier.weight(1f),
            style = TextStyle(lineHeight = 16.sp, fontSize = 16.sp)
        )
    }
    Spacer(modifier = Modifier.height(6.dp))
}