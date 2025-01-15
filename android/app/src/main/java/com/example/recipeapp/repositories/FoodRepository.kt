package com.example.recipeapp.repositories

import com.example.recipeapp.models.Food
import com.example.recipeapp.services.RetrofitInstance

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