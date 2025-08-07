package com.example.recipeapp.models.database

import java.util.UUID

data class Food(
    val name: String,
    val barcode: String,
    val servingSize: Int,
    val calories: Double,
    val carbs: Double,
    val protein: Double,
    val fat: Double,
    val createdBy: UUID,
    val editedBy: UUID,
    val created: String? = null,
    val edited: String? = null,
    val id: UUID? = null,
)