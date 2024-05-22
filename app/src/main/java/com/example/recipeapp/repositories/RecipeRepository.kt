package com.example.recipeapp.repositories

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.recipeapp.BuildConfig.API_KEY
import com.example.recipeapp.services.RetrofitInstance
import com.example.recipeapp.utils.SharedPreferencesManager
import com.example.recipeapp.api.Recipe
import com.example.recipeapp.api.SearchResponse
import com.example.recipeapp.utils.AddableIngredient
import com.example.recipeapp.utils.CachedRecipe
import retrofit2.Response

object RecipeRepository: ViewModel() {
    private val TODAYS_SPECIALS = listOf("breakfast", "lunch", "dinner", "snack")

    private var _specials: SnapshotStateList<CachedRecipe> = mutableStateListOf()
    val specials: List<CachedRecipe> get() = _specials

    private var _searchResults: SnapshotStateList<CachedRecipe> = mutableStateListOf()
    val searchResults: List<CachedRecipe> get() = _searchResults

    private var _favourites: SnapshotStateList<CachedRecipe> = mutableStateListOf()
    val favourites: List<CachedRecipe> get() = _favourites

    private var _selectedRecipe: Recipe? = null
    val selectedRecipe: Recipe? get() = _selectedRecipe

    private var _ownRecipes: SnapshotStateList<Recipe> = mutableStateListOf()
    val ownRecipes: List<Recipe> get() = _ownRecipes

    private var _recipeInAddition: Recipe? = null
    val recipeInAddition: Recipe? get() = _recipeInAddition

    private suspend fun fetchRandomMeal(context: Context, mealType: String): Recipe? {
        val response = RetrofitInstance().recipeService.getRandomRecipes(
            apiKey = API_KEY,
            includeTags = mealType,
            number = 1
        )

        if (response.isSuccessful) {
            val recipe: Recipe? = response.body()?.recipes?.get(0)
            if (recipe != null) return recipe
        } else {
            Log.d("API", "Error in fetching: ${response.errorBody()?.string()}")
        }
        return null
    }

    suspend fun fetchRandomRecipes(context: Context) {
        try {
            _specials.clear()
            if (SharedPreferencesManager.isTodaysSpecialsLoaded(context)) {
                SharedPreferencesManager.getTodaysSpecials(context).forEach {
                    _specials.add(it)
                }
            } else {
                val newSpecials: List<CachedRecipe?> = TODAYS_SPECIALS.map {
                    val recipe: Recipe? = fetchRandomMeal(context, it)
                    if (recipe != null) {
                        val savableRecipe = CachedRecipe(
                            id = recipe.id,
                            image = recipe.image,
                            title = recipe.title
                        )
                        _specials.add(savableRecipe)
                        savableRecipe
                    } else {
                        null
                    }
                }
                SharedPreferencesManager.saveTodaysSpecials(context, newSpecials)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun fetchRecipeById(context: Context, id: Int) {
        try {
            val response: Response<Recipe> = RetrofitInstance().recipeService.getRecipeInformation(
                apiKey = API_KEY,
                id = id
            )
            if (response.isSuccessful) {
                _selectedRecipe = response.body()
            } else {
                Log.d("API", "Error in fetching: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setSelectedRecipe(recipe: Recipe) {
        _selectedRecipe = recipe
    }

    suspend fun searchRecipes(context: Context, query: String) {
        try {
            _searchResults.clear()
            val response: Response<SearchResponse> = RetrofitInstance().recipeService.searchRecipes(
                apiKey = API_KEY,
                query = query,
                number = 10
            )
            if (response.isSuccessful) {
                response.body()?.results?.map {
                    _searchResults.add(
                        CachedRecipe(
                            id = it.id,
                            image = it.image,
                            title = it.title
                        )
                    )
                }
            } else {
                Log.d("API", "Error in fetching: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun fetchFavourites(context: Context) {
        val favourites = SharedPreferencesManager.getFavourites(context)
        if (favourites.isNotEmpty()) {
            for (recipe in favourites) {
                _favourites.add(recipe)
            }
        }
    }

    fun addFavourite(context: Context, recipe: Recipe) {
        _favourites.add(
            CachedRecipe(
                id = recipe.id,
                image = recipe.image,
                title = recipe.title
            )
        )
        SharedPreferencesManager.updateFavourites(context, _favourites)
    }

    fun deleteFavourite(context: Context, recipe: Recipe) {
        val newFavourites = _favourites.filter { it.id != recipe.id }
        _favourites.clear()
        newFavourites.forEach {
            _favourites.add(it)
        }
        SharedPreferencesManager.updateFavourites(context, _favourites)
    }

    fun fetchOwnRecipes(context: Context) {
        val ownRecipes = SharedPreferencesManager.getOwnRecipes(context)
        for (recipe in ownRecipes) {
            _ownRecipes.add(recipe)
        }
    }

    fun addOwnRecipe(context: Context, recipe: Recipe) {
        _ownRecipes.add(recipe)
        SharedPreferencesManager.updateOwnRecipe(context, _ownRecipes)
    }

    fun deleteOwnRecipe(context: Context): Boolean {
        val indexOfDeletable = _ownRecipes.indexOf(_selectedRecipe)
        _ownRecipes.removeAt(indexOfDeletable)
        return SharedPreferencesManager.updateOwnRecipe(context, _ownRecipes)
    }

    fun setRecipeInAddition(newRecipe: Recipe?) {
        _recipeInAddition = newRecipe
    }
}