package com.example.recipeapp.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    private val baseUrlSpoonacular = "https://api.spoonacular.com/"
    private val baseUrlDb = "https://recipeapp-api-amejacd4cjf2gqep.swedencentral-01.azurewebsites.net"

    private val retrofitSpoonacular = Retrofit.Builder()
        .baseUrl(baseUrlSpoonacular)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitDatabase = Retrofit.Builder()
        .baseUrl(baseUrlDb)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val recipeService: RecipeService = retrofitSpoonacular.create(RecipeService::class.java)
    val logsService: LogService = retrofitDatabase.create(LogService::class.java)
    val foodService: FoodService = retrofitDatabase.create(FoodService::class.java)
    val authService: AuthService = retrofitDatabase.create(AuthService::class.java)
}
