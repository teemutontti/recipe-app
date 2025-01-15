package com.example.recipeapp.repositories

import com.example.recipeapp.models.Food
import com.example.recipeapp.services.RetrofitInstance
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodService {
    @GET("api/foods")
    suspend fun getFoods(): Response<List<Food>>

    @GET("api/foods/{id}")
    suspend fun getFoodById(@Path("id") id: Int): Response<Food>

    @GET("api/foods/query")
    suspend fun getFoodByQuery(@Query("query") query: String): Response<List<Food>>

    @POST("api/foods")
    suspend fun saveFood(@Body food: Food): Response<Food>
}

class FoodRepository {
    private val retrofitInstance = RetrofitInstance()
    private val service = retrofitInstance.foodService

    suspend fun getFoods(): List<Food>? {
        return try {
            val response = service.getFoods()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getFoodById(id: Int): Food? {
        return try {
            val response = service.getFoodById(id)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getFoodsByQuery(query: String): List<Food>? {
        return try {
            val response = service.getFoodByQuery(query)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveFood(food: Food): Boolean {
        return try {
            val response = service.saveFood(food)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}