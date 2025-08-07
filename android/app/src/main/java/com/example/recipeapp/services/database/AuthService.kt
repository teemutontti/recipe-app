package com.example.recipeapp.services

import com.example.recipeapp.models.database.AuthRequest
import com.example.recipeapp.models.database.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/register")
    suspend fun register(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("api/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<AuthResponse>
}