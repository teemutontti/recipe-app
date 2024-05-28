package com.example.recipeapp.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.models.CachedRecipe
import com.example.recipeapp.models.SharedPreferencesKeys.PREFS_NAME
import com.example.recipeapp.models.SpoonacularInstruction
import com.example.recipeapp.models.room.AppDatabase
import com.example.recipeapp.models.room.DatabaseProvider
import com.example.recipeapp.models.room.PersonalIngredient
import com.example.recipeapp.models.room.PersonalInstruction
import com.example.recipeapp.models.room.PersonalRecipe
import com.example.recipeapp.repositories.RecipeUnderInspectionRepository
import com.example.recipeapp.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RecipeUnderInspectionViewModel(application: Application): AndroidViewModel(application) {
    private val repository: RecipeUnderInspectionRepository

    init {
        val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        repository = RecipeUnderInspectionRepository(prefs)
    }

    // Recipe state encapsulation
    private var _recipe = mutableStateOf<PersonalRecipe?>(null)
    val recipe: PersonalRecipe get() = _recipe.value ?: Utils.emptyRecipe

    // Loading state encapsulation
    private val _loading = mutableStateOf(true)
    val loading: Boolean get() = _loading.value

    fun setRecipe(newRecipe: PersonalRecipe?) {
        Log.d("Underrrr", "Setting recipe: $newRecipe")
        _recipe.value = newRecipe
        _loading.value = _recipe.value == null
        Log.d("Underrrr", "Setting recipe: ${_recipe.value}")
    }

    fun fetch(recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("ASYNC", "Working in: $this")
            val newRecipe = repository.fetchRecipe(recipeId)?.toPersonalRecipe()
            setRecipe(newRecipe)
        }
    }

    fun addIngredient(ingredient: PersonalIngredient) {
        val newIngredients = recipe.ingredients.toMutableList()
        newIngredients.add(ingredient)

        val newRecipe = recipe.copy(ingredients = newIngredients)
        setRecipe(newRecipe)
    }

    fun deleteIngredient(index: Int) {
        val newIngredients = recipe.ingredients.toMutableList()
        newIngredients.removeAt(index)

        val newRecipe = recipe.copy(ingredients = newIngredients)
        setRecipe(newRecipe)
    }

    fun addInstruction(instruction: PersonalInstruction) {
        val newInstructions = recipe.instructions.toMutableList()
        newInstructions.add(instruction)

        val newRecipe = recipe.copy(instructions = newInstructions)
        setRecipe(newRecipe)
    }

    fun deleteInstruction(index: Int) {
        val newInstructions = recipe.instructions.toMutableList()
        newInstructions.removeAt(index)

        val newRecipe = recipe.copy(instructions = newInstructions)
        setRecipe(newRecipe)
    }

    fun setLoadingDone() {
        _loading.value= false
    }
}
