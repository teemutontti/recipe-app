package com.example.recipeapp.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.models.room.AppDatabase
import com.example.recipeapp.models.room.DatabaseProvider
import com.example.recipeapp.models.room.PersonalRecipe
import com.example.recipeapp.repositories.PersonalRecipeRepository
import kotlinx.coroutines.launch

class PersonalRecipesViewModel(application: Application): AndroidViewModel(application) {
    private val repository: PersonalRecipeRepository

    init {
        val database: AppDatabase = DatabaseProvider.getInstance(application.applicationContext)
        repository = PersonalRecipeRepository(database)
    }

    // Recipe state encapsulation
    private var _recipes = mutableStateListOf<PersonalRecipe>()
    val recipes get() = _recipes

    // Loading state encapsulation
    private var _loading = mutableStateOf(true)
    val loading get() = _loading.value

    init {
        viewModelScope.launch {
            val allRecipes = repository.getAllRecipes()
            _recipes.addAll(allRecipes)
            _loading.value = _recipes.size == 0
        }
    }

    fun add(recipe: PersonalRecipe) {
        viewModelScope.launch {
            repository.add(recipe)
        }
    }

    fun edit(recipe: PersonalRecipe) {
        viewModelScope.launch {
            repository.update(recipe)
        }
    }

    fun delete(recipe: PersonalRecipe) {
        viewModelScope.launch {
            repository.delete(recipe)
        }
    }
}
