package com.example.recipeapp.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.models.Recipe
import com.example.recipeapp.models.SpoonacularRecipe
import com.example.recipeapp.repositories.SearchRepository
import com.example.recipeapp.utils.Utils.emptyRecipe
import kotlinx.coroutines.launch

/**
 * ViewModel class responsible for managing the search functionality.
 *
 * This class provides methods for searching recipes based on a query string and encapsulates the search results,
 * loading state, and error state.
 *
 * @property application The application context associated with the ViewModel.
 */
class SearchViewModel(application: Application): AndroidViewModel(application) {
    private val repository: SearchRepository = SearchRepository()

    // Search results state encapsulation
    private val _searchResults = mutableStateListOf<SpoonacularRecipe>()
    val searchResults: List<Recipe?> get() = _searchResults.map { it.toRecipe() ?: emptyRecipe } ?: listOf()

    // Loading state encapsulation
    private val _loading = mutableStateOf(true)
    val loading get() = _loading.value

    private val _error = mutableStateOf<String?>(null)
    val error get() = _error.value

    /**
     * Updates the search results with the provided [newSearchResult].
     *
     * @param newSearchResult The new list of search results to be set.
     */
    private fun update(newSearchResult: List<SpoonacularRecipe>) {
        _searchResults.clear()
        _searchResults.addAll(newSearchResult)
    }

    /**
     * Performs a search based on the provided [query].
     * Updates the search results accordingly and sets the loading and error states.
     *
     * @param query The search query string.
     */
    fun search(query: String) {
        viewModelScope.launch {
            val responseHandler = repository.search(query)
            if (responseHandler.success != null) {
                update(responseHandler.success)
            } else {
                _error.value = responseHandler.error
            }
            _loading.value = false
        }
    }
}
