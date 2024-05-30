package com.example.recipeapp.repositories

import android.content.SharedPreferences
import com.example.recipeapp.BuildConfig.API_KEY
import com.example.recipeapp.models.Recipe
import com.example.recipeapp.models.SharedPreferencesManager
import com.example.recipeapp.models.room.FavouriteRecipe
import com.example.recipeapp.services.RetrofitInstance
import com.example.recipeapp.utils.Utils.SPECIAL_MEAL_TYPES
import com.example.recipeapp.utils.Utils.toRecipe

class TodaysSpecialsRepository(private val prefs: SharedPreferences) {
    suspend fun getTodaysSpecials(): List<FavouriteRecipe>? {
        val isLoaded = SharedPreferencesManager.isTodaysSpecialsLoaded(prefs)
        if (isLoaded) return SharedPreferencesManager.getTodaysSpecials(prefs)
        else {
            val newRecipes = SPECIAL_MEAL_TYPES.map {
                val response = RetrofitInstance().recipeService.getRandomRecipes(API_KEY, it, 1)
                val recipe = response.body()?.recipes?.get(0)

                if (response.isSuccessful && recipe != null) {
                    recipe.toSavable()
                } else {
                    // Return null for the whole function if fetch is unsuccessful
                    return null
                }
            }
            SharedPreferencesManager.saveTodaysSpecials(prefs, newRecipes)
            return newRecipes
        }
    }
}
