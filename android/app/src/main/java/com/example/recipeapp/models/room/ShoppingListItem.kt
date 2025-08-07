package com.example.recipeapp.models.room

data class ShoppingListItem(
    val name: String,
    val note: String,
    val checked: Boolean = false,
)