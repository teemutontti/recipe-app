package com.example.recipeapp.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.models.Recipe
import com.example.recipeapp.models.SharedPreferencesKeys.PREFS_NAME
import com.example.recipeapp.models.SpoonacularRecipe
import com.example.recipeapp.models.room.FavouriteRecipe
import com.example.recipeapp.repositories.TodaysSpecialsRepository
import com.example.recipeapp.utils.Utils.toRecipe
import kotlinx.coroutines.launch

class TodaysSpecialsViewModel(application: Application): AndroidViewModel(application) {
    private val repository: TodaysSpecialsRepository

    init {
        val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        repository = TodaysSpecialsRepository(prefs)
    }

    // Specials state encapsulation
    private val _recipes = mutableStateListOf<FavouriteRecipe>()
    val recipes: List<Recipe> get() = _recipes.map { it.toRecipe() }

    // Loading state encapsulation
    private val _loading = mutableStateOf(true) // Initializing to true
    val loading get() = _loading.value

    init {
        viewModelScope.launch {
            val newRecipes = repository.getTodaysSpecials()
            if (newRecipes != null) {
                _recipes.addAll(newRecipes)
                _loading.value = _recipes.size == 0
            }
        }
    }
}