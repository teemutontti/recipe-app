package com.example.recipeapp.services

import com.example.recipeapp.api.Instructions
import com.example.recipeapp.api.Recipe
import com.example.recipeapp.api.SearchResponse
import com.example.recipeapp.api.SpoonacularResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class RetrofitInstance {
    private val BASE_URL = "https://api.spoonacular.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val recipeService: RecipeService = retrofit.create(RecipeService::class.java)
}

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
    ): Response<Recipe>

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("apiKey") apiKey: String,
        @Query("query") query: String,
        @Query("number") number: Int
    ): Response<SearchResponse>
}
