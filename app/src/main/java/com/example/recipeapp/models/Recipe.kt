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
    fun toPersonal(): PersonalRecipe {
        return if (id == -1) {
            PersonalRecipe(title, image, servings, ingredients, instructions)
        } else {
            PersonalRecipe(title, image, servings, ingredients, instructions, id)
        }
    }
    fun toFavourite() = FavouriteRecipe(id, image, title)
}
