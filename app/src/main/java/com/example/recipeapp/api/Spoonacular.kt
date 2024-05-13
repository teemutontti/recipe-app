package com.example.recipeapp.api

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
    val extendedIngredients: List<Ingredient>
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