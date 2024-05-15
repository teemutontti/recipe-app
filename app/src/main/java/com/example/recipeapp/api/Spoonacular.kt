package com.example.recipeapp.api

import android.health.connect.datatypes.units.Length
import android.health.connect.datatypes.units.Temperature

data class SpoonacularResponse(
    val recipes: List<Recipe>
)

data class Recipe(
    val id: Number,
    val title: String,
    val image: String,
    val servings: Number,
    val readyInMinutes: Number,
    val license: String,
    val sourceName: String,
    val sourceUrl: String,
    val extendedIngredients: List<Ingredient>,
    val instructions: String,
    val analyzedInstructions: List<Instructions>,
)

data class Ingredient(
    val amount: Number,
    val id: Number,
    val image: String,
    val measures: Measures,
    val name: String
)

data class Measures(
    val metric: SingleMeasure,
    val us: SingleMeasure,
)

data class SingleMeasure(
    val amount: Number,
    val unitLong: String,
    val unitShort: String
)

data class Instructions(
    val name: String,
    val steps: List<Step>
)

data class Step(
    val equipment: List<Equipment>,
    val ingredients: List<InstructionsIngredient>,
    val number: Number,
    val step: String,
    val length: LengthUnit? = null,
)

data class LengthUnit(
    val number: Number,
    val unit: String
)

data class Equipment(
    val id: Number,
    val image: String,
    val name: String,
    val temperature: TemperatureUnit? = null
)

data class TemperatureUnit(
    val number: Number,
    val unit: String
)

data class InstructionsIngredient(
    val id: Number,
    val image: String,
    val name: String
)