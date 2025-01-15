package com.example.recipeapp.models

data class Food(
    val name: String,
    val barcode: String,
    val servingSize: Int,
    val calories: Int,
    val carbs: Float,
    val protein: Float,
    val fat: Float,
    val createdBy: Int,
    val editedBy: Int,
    val created: String? = null,
    val edited: String? = null,
    val id: Int? = null,
)