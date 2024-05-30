package com.example.recipeapp.viewmodels

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.models.Recipe
import com.example.recipeapp.models.room.DatabaseProvider
import com.example.recipeapp.models.room.FavouriteRecipe
import com.example.recipeapp.repositories.FavouriteRecipeRepository
import com.example.recipeapp.utils.Utils.toRecipe
import kotlinx.coroutines.launch

class FavouriteRecipesViewModel(application: Application): AndroidViewModel(application), RecipesViewModel {
    private val database = DatabaseProvider.getInstance(application.applicationContext)
    private val repository: FavouriteRecipeRepository = FavouriteRecipeRepository(database)

    private var _recipes = mutableStateListOf<FavouriteRecipe>()
    private var _loading = mutableStateOf<Boolean>(true)
    private var _error = mutableStateOf<String?>(null)

    init {
        loadData()
    }

    override val recipes get() = _recipes.map { it.toRecipe() }
    override val loading get() = _loading.value
    override val error get() = _error.value

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

    override fun add(r: Recipe) {
        _loading.value = true
        viewModelScope.launch {
            val favouriteRecipe = r.toFavourite()
            val responseHandler = repository.add(favouriteRecipe)
            if (responseHandler.error != null) _error.value = responseHandler.error
            loadData()
        }
    }

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
