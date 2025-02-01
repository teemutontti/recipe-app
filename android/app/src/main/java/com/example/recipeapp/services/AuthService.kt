package com.example.recipeapp.services

import com.example.recipeapp.models.Auth
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class AuthResponse(val token: String, val userId: Int, val userEmail: String)

interface AuthService {
    @POST("api/register")
    suspend fun register(@Body auth: Auth): Response<AuthResponse>

    @POST("api/login")
    suspend fun login(@Body auth: Auth): Response<AuthResponse>
}