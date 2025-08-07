package com.example.recipeapp.ui.screens.recipe

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipeapp.models.database.Ingredient
import com.example.recipeapp.ui.components.inputs.IngredientForm
import com.example.recipeapp.ui.components.layout.IngredientRow
import com.example.recipeapp.viewmodels.RecipeUnderInspectionViewModel

/**
 * Composable function for rendering the ingredients step of the recipe editor.
 *
 * @param viewModel The [RecipeUnderInspectionViewModel] containing the recipe being edited.
 * @param handleAllowNextChange Callback function to handle changes in allowing navigation to the next step.
 */
@Composable
internal fun IngredientsStep(
    viewModel: RecipeUnderInspectionViewModel,
    handleAllowNextChange: (Boolean) -> Unit
) {
    LaunchedEffect(viewModel.recipe.value) {
        if (viewModel.recipe.value.ingredients.isEmpty()) {
            handleAllowNextChange(false)
        } else {
            handleAllowNextChange(true)
        }
    }

    fun handleIngredientAdd(ingredient: Ingredient) {
        // TODO: Clear data on save
        viewModel.addIngredient(ingredient)
    }

    fun handleIngredientDelete(index: Int) {
        viewModel.deleteIngredient(index)
    }

    IngredientForm { handleIngredientAdd(it) }

    Spacer(modifier = Modifier.height(16.dp))

    if (viewModel.recipe.value.ingredients.isNotEmpty()) {
        Text("Current ingredients:")
        Spacer(modifier = Modifier.height(8.dp))
        viewModel.recipe.value.ingredients.mapIndexed { index, ingredient ->
            IngredientRow(index, ingredient, handleDelete = { handleIngredientDelete(index) })
        }
    }
}