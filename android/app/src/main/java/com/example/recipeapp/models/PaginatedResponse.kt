package com.example.recipeapp.models

data class PaginatedResponse<T>(
    val content: List<T>,
    val totalElements: Long,
    val totalPages: Int,
    val size: Int,
    val number: Int
)