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
    private val repository: PersonalRecipeRepository
    private var _recipes: SnapshotStateList<PersonalRecipe>
    private var _loading: MutableState<Boolean>

    init {
        val database: AppDatabase = DatabaseProvider.getInstance(application.applicationContext)
        repository = PersonalRecipeRepository(database)
        _recipes = mutableStateListOf()
        _loading = mutableStateOf(true)
        loadData()
    }

    // State getters
    override val recipes: List<Recipe> get() = _recipes.map { it.toRecipe() }
    override val loading: Boolean get() = _loading.value

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
            val personalRecipe = r.toPersonal()
            repository.add(personalRecipe)
            loadData()
        }
    }

    override fun delete(r: Recipe) {
        _loading.value = true
        viewModelScope.launch {
            val recipe = repository.getById(r.id)
            if (recipe != null) {
                repository.delete(recipe)
                loadData()
            }
        }
    }

    fun edit(r: Recipe) {
        _loading.value = true
        viewModelScope.launch {
            val recipe = repository.getById(r.id)
            if (recipe != null) {
                repository.update(recipe)
                loadData()
            }
        }
    }

    fun isRecipeInDatabase(recipe: Recipe, callback: (Boolean) -> Unit) {
        _loading.value = true
        viewModelScope.launch {
            val result = repository.isRecipeInDatabase(recipe.id)
            callback(result)
            loadData()
        }
    }
}
