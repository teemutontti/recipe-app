package com.example.recipeapp.repositories

import android.content.SharedPreferences
import android.util.Log
import com.example.recipeapp.BuildConfig
import com.example.recipeapp.models.room.PersonalRecipe
import com.example.recipeapp.models.SharedPreferencesManager
import com.example.recipeapp.models.SpoonacularRecipe
import com.example.recipeapp.services.RetrofitInstance

class RecipeUnderInspectionRepository {
    suspend fun fetchRecipe(recipeId: Int): SpoonacularRecipe? {
        val response = RetrofitInstance().recipeService.getRecipeInformation(
            apiKey = BuildConfig.API_KEY,
            id = recipeId,
        )
        Log.d("RecipeFetchResponse", "Response: $response")
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }
}