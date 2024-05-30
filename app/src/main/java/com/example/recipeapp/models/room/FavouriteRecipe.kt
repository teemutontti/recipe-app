package com.example.recipeapp.models.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_recipes")
data class FavouriteRecipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val image: String,
    val title: String,
)
