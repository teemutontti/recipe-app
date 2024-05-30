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
import com.example.recipeapp.repositories.ManageableRecipeRepository
import kotlinx.coroutines.launch

interface RecipesViewModel {
    val recipes: List<Recipe>
    val loading: Boolean
    val error: String?
    fun loadData()
    fun add(r: Recipe)
    fun delete(r: Recipe)
}

/*
abstract class Base<T>(application: Application): AndroidViewModel(application), RecipesViewModel {
    protected abstract val repository: ManageableRecipeRepository<T>
    private var _recipes: SnapshotStateList<T> = mutableStateListOf()
    private var _loading: MutableState<Boolean> = mutableStateOf(true)

    init {
        loadData()
    }

    abstract val recipes: List<Recipe>
    abstract val loading: Boolean

    fun loadData() {
        viewModelScope.launch {
            val allRecipes = repository.getAll()
            _recipes.clear()
            _recipes.addAll(allRecipes)
            _loading.value = false
        }
    }

    fun add(r: Recipe) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val personalRecipe = r.toPersonal()
                repository.add(personalRecipe as T)
                loadData()
            } catch (e: Exception) {
                Log.d("EXCEPTION", "Error on add: $e")
            }
        }
    }

    fun delete(r: Recipe) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val personalRecipe = r.toPersonal() as T
                repository.delete(personalRecipe as T)
                loadData()
            } catch (e: Exception) {
                Log.d("EXCEPTION", "Error on delete: $e")
            }
        }
    }
}
*/
