package com.example.recipeapp.repositories

import com.example.recipeapp.BuildConfig.API_KEY
import com.example.recipeapp.models.SpoonacularRecipe
import com.example.recipeapp.services.RetrofitInstance

class SearchRepository {
    suspend fun search(query: String): ResponseHandler<List<SpoonacularRecipe>> {
        try {
            val response = RetrofitInstance().recipeService.searchRecipes(API_KEY, query, number = 10)
            if (response.isSuccessful) {
                response.body()?.let {
                    return ResponseHandler(success = it.results)
                }
            }
            return when (response.code()) {
                401 -> ResponseHandler(error = "Unauthorized! Contact the developer!")
                402 -> ResponseHandler(error = "Limit reached! Contact the developer!")
                else -> ResponseHandler(error = "Error with the code of ${response.code()} occurred!")
            }
        } catch (e: Exception) {
            return ResponseHandler(error = "Internal error occurred! Make sure you are connected to the internet.")
        }
    }
}