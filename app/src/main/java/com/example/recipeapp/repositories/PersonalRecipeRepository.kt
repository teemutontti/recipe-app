package com.example.recipeapp.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.recipeapp.models.room.PersonalRecipe
import com.example.recipeapp.models.room.AppDatabase
import com.example.recipeapp.models.room.PersonalRecipeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersonalRecipeRepository(db: AppDatabase): RecipeRepository<PersonalRecipe> {
    private val personalRecipeDao: PersonalRecipeDao = db.personalRecipeDao()

    suspend fun getAllRecipes(): List<PersonalRecipe> {
        return withContext(Dispatchers.IO) {
            personalRecipeDao.getAll()
        }
    }

    override suspend fun add(recipe: PersonalRecipe) {
        personalRecipeDao.insertRecipe(recipe)
    }

    override suspend fun update(recipe: PersonalRecipe) {
        personalRecipeDao.updateRecipe(recipe)
    }

    override suspend fun delete(recipe: PersonalRecipe) {
        personalRecipeDao.deleteRecipe(recipe)
    }
}