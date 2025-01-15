package com.example.recipeapp.utils


import com.example.recipeapp.R
import com.example.recipeapp.models.Category
import com.example.recipeapp.models.Recipe
import com.example.recipeapp.models.Food

/**
 * Utility functions and constants used throughout the application.
 */
object Constants {
    val INGREDIENT_UNITS = listOf("ml", "l", "tsp", "tbsp", "mg", "g", "kg", "pinch", "piece")
    const val LANDSCAPE_ASPECT_RATIO: Float = 1.7777778f
    const val IMAGE_WIDTH: Int = 288
    const val IMAGE_HEIGHT: Int = 162

    val categories = listOf(
        Category(R.drawable.chicken, "Chicken", "chicken"),
        Category(R.drawable.beef, "Beef", "beef"),
        Category(R.drawable.pork, "Pork", "pork"),
        Category(R.drawable.fish, "Seafood", "seafood"),
        Category(R.drawable.spaghetti, "Pasta", "pasta"),
        Category(R.drawable.rice, "Rice", "rice"),
        Category(R.drawable.vegetable, "Vegetable", "vegetable"),
        Category(R.drawable.fruit, "Fruit", "fruit"),
    )
}

