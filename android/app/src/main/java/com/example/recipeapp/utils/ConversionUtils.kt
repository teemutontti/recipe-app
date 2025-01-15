package com.example.recipeapp.utils

import android.util.Log
import com.example.recipeapp.models.FavouriteRecipe
import com.example.recipeapp.models.Food
import com.example.recipeapp.models.Fraction
import com.example.recipeapp.models.PersonalRecipe
import com.example.recipeapp.models.Recipe

object ConversionUtils {
    val emptyRecipe = Recipe(-1, "", "", 1, emptyList(), emptyList())
    val emptyFood = Food("","",0,0,0f,0f,0f,-1,-1)

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

    private fun formatFloatToString(number: Float): String {
        if (number < 1) {
            return String.format("%.1f", number).replace(",", ".")
        } else {
            return String.format("%.0f", number)
        }
    }
}