package com.example.recipeapp.repositories

import com.example.recipeapp.BuildConfig.API_KEY
import com.example.recipeapp.models.SpoonacularRecipe
import com.example.recipeapp.services.RetrofitInstance

class SearchRepository {
    suspend fun search(query: String): List<SpoonacularRecipe> {
        val response = RetrofitInstance().recipeService.searchRecipes(
            apiKey = API_KEY,
            query = query,
            number = 10
        )
        if (response.isSuccessful) {
            return response.body()?.results ?: emptyList()
        }
        return emptyList()
    }
}