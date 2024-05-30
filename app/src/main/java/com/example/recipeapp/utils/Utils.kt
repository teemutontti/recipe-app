package com.example.recipeapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.recipeapp.models.Recipe
import com.example.recipeapp.models.room.FavouriteRecipe
import com.example.recipeapp.models.room.PersonalRecipe
import com.example.recipeapp.utils.Utils.emptyRecipe

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
    val SPECIAL_MEAL_TYPES = listOf("breakfast", "lunch", "dinner", "snack")
    val DEFAULT_TEXT_STYLE = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal)
    val SMALL_HEADING_STYLE = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
    val LARGE_HEADING_STYLE = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold, )
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
        servings = 1,
        ingredients = emptyList(),
        instructions = emptyList(),
    )

    // Conversion function for Jetpack Room Entity class
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

data class Fraction(
    val range: ClosedFloatingPointRange<Float>,
    val fraction: String
)
