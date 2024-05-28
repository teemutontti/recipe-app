package com.example.recipeapp.repositories

import android.content.SharedPreferences
import com.example.recipeapp.BuildConfig.API_KEY
import com.example.recipeapp.models.CachedRecipe
import com.example.recipeapp.models.SharedPreferencesManager
import com.example.recipeapp.models.SpoonacularRecipe
import com.example.recipeapp.services.RetrofitInstance
import com.example.recipeapp.utils.Utils

class TodaysSpecialsRepository(private val prefs: SharedPreferences) {
    fun setTodaysSpecials(specials: List<CachedRecipe>) {
        SharedPreferencesManager.saveTodaysSpecials(prefs, specials)
    }

    suspend fun getTodaysSpecials(): List<CachedRecipe>? {
        if (SharedPreferencesManager.isTodaysSpecialsLoaded(prefs)) {
            return SharedPreferencesManager.getTodaysSpecials(prefs)
        } else {
            val newSpecials = mutableListOf<CachedRecipe>()
            Utils.SPECIAL_MEAL_TYPES.forEach {
                val response = RetrofitInstance().recipeService.getRandomRecipes(
                    apiKey = API_KEY,
                    includeTags = it,
                    number = 1
                )
                if (response.isSuccessful) {
                    response.body()?.recipes?.get(0)?.let { recipe: SpoonacularRecipe ->
                        newSpecials.add(recipe.toCachedRecipe())
                    }
                }
            }
            SharedPreferencesManager.saveTodaysSpecials(prefs, newSpecials)
            return newSpecials
        }
    }
}
