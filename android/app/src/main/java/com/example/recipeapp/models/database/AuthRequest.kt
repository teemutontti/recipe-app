package com.example.recipeapp.models.database

import java.util.UUID

data class AuthRequest(
    val email: String,
    val password: String,
)

data class AuthResponse(
    val token: String,
    val userId: UUID,
    val userEmail: String,
)
