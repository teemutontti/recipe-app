package com.example.recipeapp.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.models.Recipe
import com.example.recipeapp.models.SpoonacularRecipe
import com.example.recipeapp.repositories.SearchRepository
import kotlinx.coroutines.launch

class SearchViewModel(application: Application): AndroidViewModel(application) {
    private val repository: SearchRepository = SearchRepository()

    // Search results state encapsulation
    private val _searchResults = mutableStateListOf<SpoonacularRecipe>()
    val searchResults: List<Recipe> get() = _searchResults.map { it.toRecipe() }

    // Loading state encapsulation
    private val _loading = mutableStateOf(true)
    val loading get() = _loading.value

    private fun update(newSearchResult: List<SpoonacularRecipe>) {
        _searchResults.clear()
        _searchResults.addAll(newSearchResult)
    }

    fun search(query: String) {
        viewModelScope.launch {
            val newSearchResults = repository.search(query)
            update(newSearchResults)
            _loading.value = false
        }
    }
}