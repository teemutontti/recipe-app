package com.example.recipeapp.models.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PersonalRecipeDao {
    @Query("SELECT * FROM personal_recipes")
    suspend fun getAll(): List<PersonalRecipe>

    @Query("SELECT * FROM personal_recipes WHERE id == :recipeId")
    suspend fun getRecipeById(recipeId: Int): PersonalRecipe?

    @Insert
    suspend fun insertRecipe(vararg recipes: PersonalRecipe)

    @Update
    suspend fun updateRecipe(vararg recipes: PersonalRecipe)

    @Delete
    suspend fun deleteRecipe(recipe: PersonalRecipe)

    @Query("DELETE FROM personal_recipes")
    suspend fun clearData()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'personal_recipes'")
    suspend fun resetAutoIncrement()
}