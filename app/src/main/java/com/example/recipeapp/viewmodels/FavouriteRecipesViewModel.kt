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
    private val repository: FavouriteRecipeRepository
    private var _recipes: SnapshotStateList<FavouriteRecipe>
    private var _loading: MutableState<Boolean>

    init {
        val database = DatabaseProvider.getInstance(application.applicationContext)
        repository = FavouriteRecipeRepository(database)
        _recipes = mutableStateListOf()
        _loading = mutableStateOf(true)
        loadData()
    }

    // State getters
    override val recipes get() = _recipes.map { it.toRecipe() }
    override val loading get() = _loading.value

    override fun loadData() {
        viewModelScope.launch {
            val allRecipes = repository.getAll()
            _recipes.clear()
            _recipes.addAll(allRecipes)
            _loading.value = false
        }
    }

    override fun add(r: Recipe) {
        _loading.value = true
        viewModelScope.launch {
            val favouriteRecipe = r.toFavourite()
            repository.add(favouriteRecipe)
            loadData()
        }
    }

    override fun delete(r: Recipe) {
        _loading.value = true
        viewModelScope.launch {
            val favouriteRecipe = r.toFavourite()
            repository.delete(favouriteRecipe)
            loadData()
        }
    }
}
