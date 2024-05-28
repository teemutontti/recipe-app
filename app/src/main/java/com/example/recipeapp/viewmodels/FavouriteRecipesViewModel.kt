package com.example.recipeapp.viewmodels

import android.app.Application
import android.text.Spannable.Factory
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.recipeapp.models.room.AppDatabase
import com.example.recipeapp.models.room.DatabaseProvider
import com.example.recipeapp.models.room.FavouriteRecipe
import com.example.recipeapp.repositories.FavouriteRecipeRepository
import kotlinx.coroutines.launch

class FavouriteRecipesViewModel(application: Application): AndroidViewModel(application) {
    private var repository: FavouriteRecipeRepository

    init {
        val database = DatabaseProvider.getInstance(application.applicationContext)
        repository = FavouriteRecipeRepository(database)
    }

    // Recipe state encapsulation
    private var _recipes = mutableStateListOf<FavouriteRecipe>()
    val recipes get() = _recipes

    // Loading state encapsulation
    private var _loading = mutableStateOf(true)
    val loading get() = _loading.value

    init {
        viewModelScope.launch {
            val allRecipes = repository.getAllRecipes()
            _recipes.addAll(allRecipes)
            _loading.value = false
        }
    }

    fun add(recipe: FavouriteRecipe) {
        viewModelScope.launch {
            repository.add(recipe)
        }
    }

    fun edit(recipe: FavouriteRecipe) {
        viewModelScope.launch {
            repository.update(recipe)
        }
    }

    fun delete(recipe: FavouriteRecipe) {
        viewModelScope.launch {
            repository.delete(recipe)
        }
    }
}
