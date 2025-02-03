package com.example.recipeapp.models

data class Food(
    val name: String,
    val barcode: String,
    val servingSize: Int,
    val calories: Double,
    val carbs: Double,
    val protein: Double,
    val fat: Double,
    val createdBy: Int,
    val editedBy: Int,
    val created: String? = null,
    val edited: String? = null,
    val id: Int? = null,
)