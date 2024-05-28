package com.example.recipeapp.models

interface Recipe {
    val id: Number
    val title: String
    val image: Any?
    val servings: Number
}