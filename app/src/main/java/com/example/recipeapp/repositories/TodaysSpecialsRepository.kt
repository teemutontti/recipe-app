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
    suspend fun getTodaysSpecials(): ResponseHandler<List<FavouriteRecipe>> {
        val isLoaded = SharedPreferencesManager.isTodaysSpecialsLoaded(prefs)
        if (isLoaded) {
            val newRecipes = SharedPreferencesManager.getTodaysSpecials(prefs)
            return if (newRecipes != null) {
                ResponseHandler(success = newRecipes)
            } else {
                ResponseHandler(error = "Error with getting saved specials!")
            }
        } else {
            val newRecipes = SPECIAL_MEAL_TYPES.map {
                val response = RetrofitInstance().recipeService.getRandomRecipes(API_KEY, it, 1)
                if (response.isSuccessful) {
                    response.body()?.recipes?.get(0)?.toSavable()
                } else {
                    return ResponseHandler(error = "Error with the code of ${response.code()} occurred!")
                }
            }

            SharedPreferencesManager.saveTodaysSpecials(prefs, newRecipes.filterNotNull())
            return ResponseHandler(success = newRecipes.filterNotNull())
        }
    }
}
