package com.example.recipeapp.services

import com.example.recipeapp.models.SearchResponse
import com.example.recipeapp.models.SpoonacularRecipe
import com.example.recipeapp.models.SpoonacularResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit instance for interacting with the Spoonacular API.
 */
class RetrofitInstance {
    private val BASE_URL = "https://api.spoonacular.com/"

    /**
     * Retrofit instance initialized with the base URL and GsonConverterFactory.
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * Service interface for making API calls related to recipes.
     */
    val recipeService: RecipeService = retrofit.create(RecipeService::class.java)
}

/**
 * Service interface defining methods for interacting with recipe-related endpoints of the Spoonacular API.
 */
interface RecipeService {
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("apiKey") apiKey: String,
        @Query("include-tags") includeTags: String,
        @Query("number") number: Int,
    ): Response<SpoonacularResponse>

    @GET("recipes/{id}/information")
    suspend fun getRecipeInformation(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String
    ): Response<SpoonacularRecipe>

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("apiKey") apiKey: String,
        @Query("query") query: String,
        @Query("number") number: Int
    ): Response<SearchResponse>
}
