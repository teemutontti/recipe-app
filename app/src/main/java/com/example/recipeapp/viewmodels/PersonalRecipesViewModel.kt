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

class PersonalRecipesViewModel(application: Application): AndroidViewModel(application), RecipesViewModel {
    private val database = DatabaseProvider.getInstance(application.applicationContext)
    private val repository = PersonalRecipeRepository(database)

    private var _recipes = mutableStateListOf<PersonalRecipe>()
    private var _loading = mutableStateOf(true)
    private var _error = mutableStateOf<String?>(null)

    init { loadData() }

    override val recipes: List<Recipe> get() = _recipes.map { it.toRecipe() }
    override val loading: Boolean get() = _loading.value
    override val error: String? get() = _error.value

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
            val personalRecipe = r.toPersonal()
            val responseHandler = repository.add(personalRecipe)
            if (responseHandler.error != null) _error.value = responseHandler.error
            loadData()
        }
    }

    override fun delete(r: Recipe) {
        _loading.value = true
        viewModelScope.launch {
            val responseHandler = repository.delete(r.toPersonal())
            if (responseHandler.error != null) _error.value = responseHandler.error
            loadData()
        }
    }

    fun edit(r: Recipe) {
        _loading.value = true
        viewModelScope.launch {
            val responseHandler = repository.update(r.toPersonal())
            if (responseHandler.error != null) _error.value = responseHandler.error
            loadData()
        }
    }

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
