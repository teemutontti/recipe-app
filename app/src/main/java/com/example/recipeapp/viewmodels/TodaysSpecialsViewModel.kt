package com.example.recipeapp.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.models.Recipe
import com.example.recipeapp.models.SharedPreferencesKeys.PREFS_NAME
import com.example.recipeapp.models.room.FavouriteRecipe
import com.example.recipeapp.repositories.TodaysSpecialsRepository
import com.example.recipeapp.utils.Utils.toRecipe
import kotlinx.coroutines.launch

/**
 * ViewModel class responsible for managing today's specials functionality.
 *
 * This class retrieves today's special recipes from the repository and encapsulates them along with loading and error states.
 *
 * @property application The application context associated with the ViewModel.
 */
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

    private val _error = mutableStateOf<String?>(null)
    val error get() = _error.value

    init {
        viewModelScope.launch {
            val responseHandler = repository.getTodaysSpecials()
            if (responseHandler.success != null) {
                _recipes.addAll(responseHandler.success)
            } else {
                _error.value = responseHandler.error
            }
            _loading.value = false
        }
    }
}
