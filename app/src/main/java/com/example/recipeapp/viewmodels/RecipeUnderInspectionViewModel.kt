package com.example.recipeapp.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.models.Ingredient
import com.example.recipeapp.models.Instruction
import com.example.recipeapp.models.Recipe
import com.example.recipeapp.models.SharedPreferencesKeys.PREFS_NAME
import com.example.recipeapp.repositories.RecipeUnderInspectionRepository
import com.example.recipeapp.utils.Utils.emptyRecipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecipeUnderInspectionViewModel(application: Application): AndroidViewModel(application) {
    private val repository: RecipeUnderInspectionRepository = RecipeUnderInspectionRepository()
    private var _recipe: MutableState<Recipe> = mutableStateOf(emptyRecipe)
    private var _loading: MutableState<Boolean> = mutableStateOf(true)

    val recipe: MutableState<Recipe> get() = _recipe
    val loading: MutableState<Boolean> get() = _loading

    fun setRecipe(newRecipe: Recipe?) {
        viewModelScope.launch {
            _recipe.value = newRecipe ?: emptyRecipe
            delay(125) // Adding small delay to get rid of loading bar flashing
            _loading.value = false
        }
    }

    fun fetchRecipe(id: Int) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val newRecipe = repository.fetchRecipe(id)?.toRecipe()
            setRecipe(newRecipe)
        }
    }

    fun addIngredient(ingredient: Ingredient) {
        val newIngredients = recipe.value.ingredients.toMutableList()
        newIngredients.add(ingredient)

        val newRecipe = recipe.value.copy(ingredients = newIngredients)
        setRecipe(newRecipe)
    }

    fun deleteIngredient(index: Int) {
        val newIngredients = recipe.value.ingredients.toMutableList()
        newIngredients.removeAt(index)

        val newRecipe = recipe.value.copy(ingredients = newIngredients)
        setRecipe(newRecipe)
    }

    fun addInstruction(instruction: Instruction) {
        val newInstructions = recipe.value.instructions.toMutableList()
        newInstructions.add(instruction)

        val newRecipe = recipe.value.copy(instructions = newInstructions)
        setRecipe(newRecipe)
    }

    fun deleteInstruction(index: Int) {
        val newInstructions = recipe.value.instructions.toMutableList()
        newInstructions.removeAt(index)

        val newRecipe = recipe.value.copy(instructions = newInstructions)
        setRecipe(newRecipe)
    }
}
