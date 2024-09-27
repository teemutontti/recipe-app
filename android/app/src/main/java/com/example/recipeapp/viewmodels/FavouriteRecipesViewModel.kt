package com.example.recipeapp.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.models.Recipe
import com.example.recipeapp.models.DatabaseProvider
import com.example.recipeapp.models.FavouriteRecipe
import com.example.recipeapp.repositories.FavouriteRecipeRepository
import com.example.recipeapp.utils.Utils.toRecipe
import kotlinx.coroutines.launch

/**
 * ViewModel class for managing favorite recipes.
 *
 * This class provides methods to load, add, and delete favorite recipes.
 *
 * @property application The application context.
 */
class FavouriteRecipesViewModel(application: Application): AndroidViewModel(application), RecipesViewModel {
    private val database = DatabaseProvider.getInstance(application.applicationContext)
    private val repository: FavouriteRecipeRepository = FavouriteRecipeRepository(database)

    // Mutable state variables for holding favorite recipes, loading state, and error message
    private var _recipes = mutableStateListOf<FavouriteRecipe>()
    private var _loading = mutableStateOf<Boolean>(true)
    private var _error = mutableStateOf<String?>(null)

    init {
        loadData()
    }

    // Public properties for observing favorite recipes, loading state, and error message
    override val recipes get() = _recipes.map { it.toRecipe() }
    override val loading get() = _loading.value
    override val error get() = _error.value

    /**
     * Loads favorite recipes from the repository.
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
     * Adds a recipe to the list of favorite recipes.
     *
     * @param r The recipe to add.
     */
    override fun add(r: Recipe) {
        _loading.value = true
        viewModelScope.launch {
            val favouriteRecipe = r.toFavourite()
            val responseHandler = repository.add(favouriteRecipe)
            if (responseHandler.error != null) _error.value = responseHandler.error
            loadData()
        }
    }

    /**
     * Deletes a recipe from the list of favorite recipes.
     *
     * @param r The recipe to delete.
     */
    override fun delete(r: Recipe) {
        _loading.value = true
        viewModelScope.launch {
            val favouriteRecipe = r.toFavourite()
            val responseHandler = repository.delete(favouriteRecipe)
            if (responseHandler.error != null) _error.value = responseHandler.error
            loadData()
        }
    }
}
