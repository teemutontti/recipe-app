package com.example.recipeapp.models

data class ShoppingListItem(
    val name: String,
    val note: String,
    val checked: Boolean = false,
)
