package com.example.recipeapp.repositories

import com.example.recipeapp.models.Auth
import com.example.recipeapp.services.AuthResponse
import com.example.recipeapp.services.RetrofitInstance
import com.example.recipeapp.utils.Result

class AuthRepository {
    private val service = RetrofitInstance().authService

    suspend fun register(auth: Auth): Result<AuthResponse> {
        return try {
            val response = service.register(auth)
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun login(auth: Auth): Result<AuthResponse> {
        return try {
            val response = service.login(auth)
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }
}
