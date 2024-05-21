package com.example.recipeapp.utils

data class CachedRecipe(
    val id: Number,
    val image: String,
    val title: String,
)

data class AddableIngredient(
    val name: String,
    val amount: Int,
    val unit: String
)