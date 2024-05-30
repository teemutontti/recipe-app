package com.example.recipeapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.models.Recipe
import com.example.recipeapp.models.room.AppDatabase
import com.example.recipeapp.models.room.DatabaseProvider
import com.example.recipeapp.models.room.PersonalRecipe
import com.example.recipeapp.repositories.PersonalRecipeRepository
import com.example.recipeapp.utils.Utils.toRecipe
import kotlinx.coroutines.launch

/**
 * ViewModel class for managing personal recipes.
 *
 * This class provides methods to load, add, edit, and delete personal recipes.
 *
 * @property application The application context.
 */
class PersonalRecipesViewModel(application: Application): AndroidViewModel(application), RecipesViewModel {
    private val database = DatabaseProvider.getInstance(application.applicationContext)
    private val repository = PersonalRecipeRepository(database)

    // Mutable state variables for holding personal recipes, loading state, and error message
    private var _recipes = mutableStateListOf<PersonalRecipe>()
    private var _loading = mutableStateOf(true)
    private var _error = mutableStateOf<String?>(null)

    init { loadData() }

    // Public properties for observing personal recipes, loading state, and error message
    override val recipes: List<Recipe> get() = _recipes.map { it.toRecipe() }
    override val loading: Boolean get() = _loading.value
    override val error: String? get() = _error.value

    /**
     * Loads personal recipes from the repository.
     * Updates the loading state and error message accordingly.
     */
    override fun loadData() {
        viewModelScope.launch {
            val responseHandler = repository.getAll()
            if (responseHandler.success != null) {
                _recipes.clear()
                _recipes.addAll(responseHandler.success)
            } else {
                _error.value = responseHandler.error
            }
            _loading.value = false
        }
    }

    /**
     * Adds a recipe to the list of personal recipes.
     *
     * @param r The recipe to add.
     */
    override fun add(r: Recipe) {
        _loading.value = true
        viewModelScope.launch {
            val personalRecipe = r.toPersonal()
            val responseHandler = repository.add(personalRecipe)
            if (responseHandler.error != null) _error.value = responseHandler.error
            loadData()
        }
    }

    /**
     * Deletes a recipe from the list of personal recipes.
     *
     * @param r The recipe to delete.
     */
    override fun delete(r: Recipe) {
        _loading.value = true
        viewModelScope.launch {
            val responseHandler = repository.delete(r.toPersonal())
            if (responseHandler.error != null) _error.value = responseHandler.error
            loadData()
        }
    }

    /**
     * Edits a personal recipe.
     *
     * @param r The updated recipe.
     */
    fun edit(r: Recipe) {
        _loading.value = true
        viewModelScope.launch {
            val responseHandler = repository.update(r.toPersonal())
            if (responseHandler.error != null) _error.value = responseHandler.error
            loadData()
        }
    }

    /**
     * Checks if a recipe exists in the database.
     *
     * @param recipe The recipe to check.
     * @param callback The callback function to handle the result.
     */
    fun isRecipeInDatabase(recipe: Recipe, callback: (Boolean) -> Unit) {
        _loading.value = true
        viewModelScope.launch {
            val responseHandler = repository.isRecipeInDatabase(recipe.id)
            if (responseHandler.success != null) callback(responseHandler.success)
            if (responseHandler.error != null) _error.value = responseHandler.error
            loadData()
        }
    }
}
