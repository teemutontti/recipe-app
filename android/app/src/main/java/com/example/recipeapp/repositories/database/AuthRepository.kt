package com.example.recipeapp.repositories.database

import android.util.Log
import com.example.recipeapp.models.database.AuthRequest
import com.example.recipeapp.models.database.AuthResponse
import com.example.recipeapp.services.database.RetrofitInstance
import com.example.recipeapp.utils.Result

class AuthRepository {
    private val service = RetrofitInstance().authService

    suspend fun register(authRequest: AuthRequest): Result<AuthResponse> {
        return try {
            val response = service.register(authRequest)
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun login(authRequest: AuthRequest): Result<AuthResponse> {
        return try {
            val response = service.login(authRequest)
            Log.d("AuthRepository", "$response")
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Log.d("AuthRepository", "Exception in login $e")
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }
}
