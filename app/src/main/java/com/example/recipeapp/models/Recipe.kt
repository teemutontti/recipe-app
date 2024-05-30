package com.example.recipeapp.models

import com.example.recipeapp.models.room.FavouriteRecipe
import com.example.recipeapp.models.room.PersonalRecipe

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val servings: Int,
    val ingredients: List<Ingredient>,
    val instructions: List<Instruction>,
    val isPersonalRecipe: Boolean = false,
){
    fun toPersonal() = PersonalRecipe(title, image, servings, ingredients, instructions)
    fun toFavourite() = FavouriteRecipe(id, image, title)
}
