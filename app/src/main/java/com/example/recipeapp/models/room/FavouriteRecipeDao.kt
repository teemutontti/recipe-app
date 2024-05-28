package com.example.recipeapp.models.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavouriteRecipeDao {
    @Query("SELECT * FROM favourite_recipes")
    suspend fun getAll(): List<FavouriteRecipe>

    @Query("SELECT * FROM favourite_recipes WHERE id == :userId")
    suspend fun getRecipeById(userId: Int): FavouriteRecipe

    @Insert
    suspend fun insertRecipe(vararg recipes: FavouriteRecipe)

    @Update
    suspend fun updateRecipe(vararg recipes: FavouriteRecipe)

    @Delete
    suspend fun deleteRecipe(recipe: FavouriteRecipe)
}