package com.example.recipeapp.repositories

import android.util.Log
import com.example.recipeapp.BuildConfig
import com.example.recipeapp.models.SpoonacularRecipe
import com.example.recipeapp.services.RetrofitInstance

/**
 * Repository class for fetching detailed information about a recipe under inspection from the Spoonacular API.
 */
class RecipeUnderInspectionRepository {
    /**
     * Fetches detailed information about a recipe under inspection from the Spoonacular API.
     *
     * @param recipeId The ID of the recipe to fetch.
     * @return The fetched [SpoonacularRecipe] if successful, or null if the request fails.
     */
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
