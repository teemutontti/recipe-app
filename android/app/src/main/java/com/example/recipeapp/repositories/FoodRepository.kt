package com.example.recipeapp.repositories

import android.util.Log
import com.example.recipeapp.models.Food
import com.example.recipeapp.services.RetrofitInstance
import com.example.recipeapp.utils.Result

class FoodRepository {
    private val retrofitInstance = RetrofitInstance()
    private val service = retrofitInstance.foodService

    suspend fun getFoods(): Result<List<Food>> {
        return try {
            val response = service.getFoods()
            if (response.isSuccessful && response.body() != null) Result.success(response.body())
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun getFoodById(id: Int): Result<Food> {
        return try {
            val response = service.getFoodById(id)
            if (response.isSuccessful && response.body() != null) Result.success(response.body())
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun getFoodsByQuery(query: String): Result<List<Food>> {
        return try {
            val response = service.getFoodByQuery(query)
            if (response.isSuccessful && response.body() != null) Result.success(response.body())
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun saveFood(food: Food): Result<Boolean> {
        return try {
            val response = service.saveFood(food)
            if (response.isSuccessful) Result.success(true)
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }
}