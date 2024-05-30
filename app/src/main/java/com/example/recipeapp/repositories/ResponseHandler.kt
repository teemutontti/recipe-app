package com.example.recipeapp.repositories

data class ResponseHandler<T>(
    val success: T? = null,
    val error: String? = null,
)