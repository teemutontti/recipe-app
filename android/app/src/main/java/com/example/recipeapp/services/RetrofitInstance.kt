package com.example.recipeapp.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    private val baseUrlSpoonacular = "https://api.spoonacular.com/"
    private val baseUrlDb = "https://recipeapp-api-amejacd4cjf2gqep.swedencentral-01.azurewebsites.net"
    private val baseUrlFineli = "https://fineli.fi/"

    private val retrofitSpoonacular = Retrofit.Builder()
        .baseUrl(baseUrlSpoonacular)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitDatabase = Retrofit.Builder()
        .baseUrl(baseUrlDb)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "RecipeApp")
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofitFineli = Retrofit.Builder()
        .baseUrl(baseUrlFineli)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val recipeService: RecipeService = retrofitSpoonacular.create(RecipeService::class.java)
    val logsService: LogService = retrofitDatabase.create(LogService::class.java)
    val foodService: FoodService = retrofitDatabase.create(FoodService::class.java)
    val authService: AuthService = retrofitDatabase.create(AuthService::class.java)
    val fineliService: FineliService = retrofitFineli.create(FineliService::class.java)
}
