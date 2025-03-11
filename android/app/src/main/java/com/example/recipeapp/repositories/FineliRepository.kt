package com.example.recipeapp.repositories

import android.util.Log
import com.example.recipeapp.models.FineliResponse
import com.example.recipeapp.services.RetrofitInstance
import com.example.recipeapp.utils.Result

class FineliRepository {
    private val service = RetrofitInstance().fineliService

    suspend fun getFoodsByQuery(query: String): Result<List<FineliResponse>> {
        return try {
            val response = service.getFoods(query)
            Log.d("FineliRepository", response.toString())
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun getFoodById(id: Int): Result<FineliResponse> {
        return try {
            val response = service.getFoodById(id)
            if (response.isSuccessful && response.body() != null) Result.success(response.body())
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }
}