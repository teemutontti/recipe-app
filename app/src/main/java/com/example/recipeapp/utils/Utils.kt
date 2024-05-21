package com.example.recipeapp.utils

import com.example.recipeapp.api.Ingredient
import com.example.recipeapp.api.Instructions
import com.example.recipeapp.api.Measures
import com.example.recipeapp.api.Recipe
import com.example.recipeapp.api.SingleMeasure
import com.example.recipeapp.api.Step

object Utils {
    val INGREDIENT_UNITS = listOf("ml", "l", "tsp", "tbsp", "mg", "g", "kg", "pinch", "piece")

    object Validator {
        object Ingredient {
            fun name(name: String): Boolean {
                return name.length > 2
            }
            fun amount(amount: Int): Boolean {
                return amount in 1..999
            }
            fun unit(unit: String): Boolean {
                return unit in INGREDIENT_UNITS
            }
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

    data class CachedRecipe(
        val id: Number,
        val image: String,
        val title: String,
    )
}