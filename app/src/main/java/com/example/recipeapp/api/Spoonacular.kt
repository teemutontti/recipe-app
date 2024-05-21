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
    val measures: Measures,
    var name: String
)

data class Measures(
    val metric: SingleMeasure,
    val us: SingleMeasure,
)

data class SingleMeasure(
    var amount: Number,
    var unitShort: String
)

data class Instructions(
    val name: String,
    val steps: List<Step>
)

data class Step(
    val number: Number,
    var step: String,
)

data class SearchResponse(
    val results: List<Recipe>
)