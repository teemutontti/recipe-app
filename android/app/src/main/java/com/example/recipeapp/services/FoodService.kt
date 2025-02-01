package com.example.recipeapp.services

import com.example.recipeapp.models.Food
import com.example.recipeapp.models.PaginatedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodService {
    @GET("api/foods")
    suspend fun getFoods(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Header("Authorization") authHeader: String,
    ): Response<PaginatedResponse<Food>>

    @GET("api/foods/{id}")
    suspend fun getFoodById(
        @Path("id") id: Int,
        @Header("Authorization") authHeader: String,
    ): Response<Food>

    @GET("api/foods/query")
    suspend fun getFoodByQuery(
        @Query("query") query: String,
        @Header("Authorization") authHeader: String,
    ): Response<List<Food>>

    @POST("api/foods")
    suspend fun saveFood(
        @Body food: Food,
        @Header("Authorization") authHeader: String,
    ): Response<Food>
}