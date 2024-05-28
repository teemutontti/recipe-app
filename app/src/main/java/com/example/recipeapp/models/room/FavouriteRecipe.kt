package com.example.recipeapp.models.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipeapp.models.CachedRecipe

@Entity(tableName = "favourite_recipes")
data class FavouriteRecipe(
    @PrimaryKey(autoGenerate = true) val dbId: Int = 0,
    val id: Int,
    val image: String,
    val title: String,
)