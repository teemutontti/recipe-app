package com.example.recipeapp.models.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipeapp.models.Ingredient
import com.example.recipeapp.models.Instruction

@Entity(tableName = "personal_recipes")
data class PersonalRecipe(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val image: String,
    val servings: Int,
    val ingredients: List<Ingredient>,
    val instructions: List<Instruction>,
)
