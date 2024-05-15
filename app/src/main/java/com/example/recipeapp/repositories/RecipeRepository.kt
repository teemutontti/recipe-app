package com.example.recipeapp.repositories

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.recipeapp.BuildConfig.API_KEY
import com.example.recipeapp.api.Instructions
import com.example.recipeapp.services.RetrofitInstance
import com.example.recipeapp.utils.SharedPreferencesManager
import com.example.recipeapp.api.Recipe
import com.example.recipeapp.api.SpoonacularResponse
import retrofit2.Response

object RecipeRepository: ViewModel() {
    private var _specials: SnapshotStateList<Recipe> = mutableStateListOf<Recipe>()
    val specials: List<Recipe> get() = _specials

    private var _selectedRecipe: Recipe? = null
    val selectedRecipe: Recipe? get() = _selectedRecipe

    private suspend fun fetchRandomMeal(context: Context, mealType: String): Recipe? {
        val response: Response<SpoonacularResponse> = RetrofitInstance().recipeService
            .getRandomRecipes(apiKey = API_KEY, includeTags = mealType, number = 1)

        if (response.isSuccessful) {
            // Saving to shared prefs
            val recipe: Recipe? = response.body()?.recipes?.get(0)
            if (recipe != null) {
                SharedPreferencesManager.saveTodaysSpecial(context, recipe, mealType)
                return recipe
            }
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
                SharedPreferencesManager.TODAYS_SPECIAL_MEAL_TYPES.map {
                    val recipe: Recipe? = fetchRandomMeal(context, it)
                    if (recipe != null) _specials.add(recipe)
                }
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
            if (response.isSuccessful) _selectedRecipe = response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateSelectedRecipe(recipe: Recipe) {
        _selectedRecipe = recipe
    }
}