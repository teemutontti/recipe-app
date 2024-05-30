package com.example.recipeapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.recipeapp.R
import com.example.recipeapp.models.Category
import com.example.recipeapp.models.Recipe
import com.example.recipeapp.models.FavouriteRecipe
import com.example.recipeapp.models.Fraction
import com.example.recipeapp.models.PersonalRecipe

/**
 * Utility functions and constants used throughout the application.
 */
object Utils {
    val INGREDIENT_UNITS = listOf("ml", "l", "tsp", "tbsp", "mg", "g", "kg", "pinch", "piece")
    val SPECIAL_MEAL_TYPES = listOf("breakfast", "lunch", "dinner", "snack")
    const val LANDSCAPE_ASPECT_RATIO: Float = 1.7777778f
    const val IMAGE_WIDTH: Int = 288
    const val IMAGE_HEIGHT: Int = 162

    /**
     * Validator for recipe title.
     */
    object Validator {
        fun recipeTitle(title: String): Boolean {
            return title.length > 2
        }
        fun recipeServings(servings: Int): Boolean {
            return servings in 1..40
        }
        fun ingredientName(name: String): Boolean {
            return name.length > 2
        }
        fun ingredientAmount(amount: Int): Boolean {
            return amount in 1..999
        }
        fun ingredientUnit(unit: String): Boolean {
            return unit in INGREDIENT_UNITS
        }
    }

    val emptyRecipe = Recipe(
        id = -1,
        title = "",
        image = "",
        servings = 1,
        ingredients = emptyList(),
        instructions = emptyList(),
    )

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

    // Conversion function for Jetpack Room Entity class
    /**
     * Converts a [PersonalRecipe] object to a [Recipe] object.
     * @param personalRecipe The personal recipe to convert.
     * @return The converted recipe.
     */
    fun PersonalRecipe.toRecipe(): Recipe {
        return emptyRecipe.copy(
            id = this.id,
            title = this.title,
            image = this.image,
            servings = this.servings,
            ingredients = this.ingredients,
            instructions = this.instructions,
            isPersonalRecipe = true,
        )
    }

    // Conversion function for Jetpack Room Entity class
    /**
     * Converts a [FavouriteRecipe] object to a [Recipe] object.
     * @param favouriteRecipe The favourite recipe to convert.
     * @return The converted recipe.
     */
    fun FavouriteRecipe.toRecipe(): Recipe {
        return emptyRecipe.copy(
            id = id,
            title = title,
            image = image,
        )
    }

    private fun formatFloatToString(number: Float): String {
        if (number < 1) {
            return String.format("%.1f", number).replace(",", ".")
        } else {
            return String.format("%.0f", number)
        }
    }

    /**
     * Converts a float number to a fraction if applicable.
     * @param number The number to convert.
     * @return The converted fraction or formatted number.
     */
    fun convertToFraction(number: Float): String {
        Log.d("FractionCreation", "Creating fraction for: $number")
        val decimal = number - number.toInt()
        val fractionRanges = listOf(
            Fraction(0.24f..0.26f, "1/4"),
            Fraction(0.49f..0.51f, "1/2"),
            Fraction(0.74f..0.76f, "3/4"),
            Fraction(0.32f..0.34f, "1/3"),
            Fraction(0.65f..0.67f, "2/3")
        )
        if (number < 100) {
            fractionRanges.forEach {
                if (decimal in it.range) {
                    if (number.toInt() >= 1) {
                        return "${number.toInt()} ${it.fraction}"
                    } else {
                        return it.fraction
                    }
                }
            }
        }
        return formatFloatToString(number)
    }

    /**
     * Checks if there is an active internet connection.
     * @param context The context of the application.
     * @return True if there is an active internet connection, false otherwise.
     */
    fun checkInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        if (network != null) {
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            if (capabilities != null) {
                return when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
        }
        return false
    }
}

