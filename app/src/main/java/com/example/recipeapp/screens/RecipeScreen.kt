package com.example.recipeapp.screens

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.R
import com.example.recipeapp.composables.DeleteDialog
import com.example.recipeapp.composables.IngredientRow
import com.example.recipeapp.composables.InstructionRow
import com.example.recipeapp.composables.NumberCounter
import com.example.recipeapp.composables.RecipeImage
import com.example.recipeapp.composables.UserFeedbackMessage
import com.example.recipeapp.models.room.PersonalIngredient
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
    var showServingsChangedNotice by remember { mutableStateOf(false) }
    var initialServings by remember { mutableIntStateOf(recipeUnderInspectionViewModel.recipe.servings) }

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

            // Updating initial servings state
            initialServings = recipeUnderInspectionViewModel.recipe.servings
        } else {
            recipeUnderInspectionViewModel.setLoadingDone()
        }
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
        showServingsChangedNotice = newServings != initialServings

        val ingredientsPerServing = recipe.ingredients.map {
            it.amount / recipe.servings
        }

        val newIngredientAmounts = ingredientsPerServing.map {
            it * newServings
        }

        val newIngredients = recipe.ingredients.mapIndexed { index: Int, item: PersonalIngredient ->
            item.copy(amount = newIngredientAmounts[index])
        }

        val newRecipe = recipe.copy(ingredients = newIngredients, servings = newServings)
        recipeUnderInspectionViewModel.setRecipe(newRecipe)
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
                    IconButton(
                        enabled = !preview,
                        onClick = { handleFavouriteClick() }
                    ) {
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
                        IconButton(
                            enabled = !preview,
                            onClick = { showMore = !showMore }
                        ) {
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

            /* === IMAGE SECTION === */
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

            /* === SERVINGS SECTION === */
            NumberCounter(
                value = recipeUnderInspectionViewModel.recipe.servings,
                suffix = "servings",
                onNumberChange = ::calculateIngredients,
                max = 50,
            )
            Spacer(modifier = Modifier.height(24.dp))

            /* === INGREDIENTS SECTION === */
            if (recipeUnderInspectionViewModel.recipe.ingredients.isNotEmpty()) {
                Text("Ingredients")
                Spacer(modifier = Modifier.height(8.dp))
                recipeUnderInspectionViewModel.recipe.ingredients.forEachIndexed { index, ingredient ->
                    IngredientRow(index, ingredient)
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            /* === INSTRUCTIONS SECTION === */
            if (recipeUnderInspectionViewModel.recipe.instructions.isNotEmpty()) {
                Text("Instructions")
                if (showServingsChangedNotice) {
                    Spacer(modifier = Modifier.height(8.dp))
                    UserFeedbackMessage("Serving size changed", type = "warning")
                }
                Spacer(modifier = Modifier.height(8.dp))
                recipeUnderInspectionViewModel.recipe.instructions.forEachIndexed { index, instruction ->
                    InstructionRow(index, instruction)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}