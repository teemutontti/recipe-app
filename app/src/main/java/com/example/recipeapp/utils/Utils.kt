package com.example.recipeapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.recipeapp.api.Ingredient
import com.example.recipeapp.api.Measures
import com.example.recipeapp.api.Recipe
import com.example.recipeapp.api.SingleMeasure
import com.example.recipeapp.api.Step

object Utils {
    private var _id: Int = 0

    fun setId(newId: Int) {
        _id = newId
    }

    fun getNextId(): Int {
        _id += 1
        return _id
    }

    val INGREDIENT_UNITS = listOf("ml", "l", "tsp", "tbsp", "mg", "g", "kg", "pinch", "piece")
    const val LANDSCAPE_ASPECT_RATIO: Float = 1.7777778f
    const val IMAGE_WIDTH: Int = 288
    const val IMAGE_HEIGHT: Int = 162

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
        servings = -1,
        readyInMinutes = -1,
        license = "",
        sourceName = "",
        sourceUrl = "",
        extendedIngredients = listOf(),
        instructions = "",
        analyzedInstructions = listOf()
    )
    val emptyIngredient = Ingredient(
        name = "",
        measures = Measures(
            metric = SingleMeasure(
                amount = 1.0,
                unitShort = ""
            ),
            us = SingleMeasure(
                amount = 1.0,
                unitShort = "",
            )
        ),
    )
    val emptyInstructionStep = Step(
        number = 1,
        step = ""
    )
    val emptyAddableIngredient = AddableIngredient(
        name = "",
        amount = 1,
        unit = ""
    )

    fun formatFloatToString(number: Float): String {
        val fractionStr = convertToFraction(number)
        if (number < 1) {
            if (fractionStr != null) return ""
            return String.format("%.1f", number).replace(",", ".")
        } else {
            return String.format("%.0f", number)
        }
    }

    fun convertToFraction(number: Float): String? {
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
                    return it.fraction
                }
            }
        }
        return null
    }

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