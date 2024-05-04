package com.example.recipeapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class RetrofitInstance {
    private val BASE_URL = "https://api.edamam.com/api/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val recipeService: RecipeService = retrofit.create(RecipeService::class.java)
}

interface RecipeService {
    @GET("recipes/v2")
    suspend fun getRandomRecipes(
        @Query("type") type: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("random") random: Boolean,
        @Query("mealType") mealType: String
    ): EdamamResponse
}

data class EdamamResponse(
    val count: Int,
    val hits: List<Hit>
)
data class Hit(
    val recipe: Recipe
)

data class Recipe(
    val label: String,
    val image: String,
    val ingredients: List<Ingredient>,
    val calories: Number,
)
data class Ingredient(
    val text: String,
    val quantity: String,
    val measure: String,
    val food: String,
    val weight: Number,
    val foodId: String
)