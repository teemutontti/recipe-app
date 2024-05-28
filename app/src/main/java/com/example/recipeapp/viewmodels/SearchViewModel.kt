package com.example.recipeapp.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.models.CachedRecipe
import com.example.recipeapp.models.SharedPreferencesKeys.PREFS_NAME
import com.example.recipeapp.repositories.SearchRepository
import kotlinx.coroutines.launch

class SearchViewModel(application: Application): AndroidViewModel(application) {
    private val repository: SearchRepository = SearchRepository()

    // Search results state encapsulation
    private val _searchResults = mutableListOf<CachedRecipe>()
    val searchResults get() = _searchResults

    // Loading state encapsulation
    private val _loading = mutableStateOf(true)
    val loading get() = _loading.value

    private fun update(newSearchResult: List<CachedRecipe>) {
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