package com.example.recipeapp.repositories

import android.util.Log
import com.example.recipeapp.models.room.PersonalRecipe
import com.example.recipeapp.models.room.AppDatabase
import com.example.recipeapp.models.room.PersonalRecipeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersonalRecipeRepository(db: AppDatabase): ManageableRecipeRepository<PersonalRecipe> {
    private val personalRecipeDao: PersonalRecipeDao = db.personalRecipeDao()

    // Data fetching functions
    override suspend fun getAll(): ResponseHandler<List<PersonalRecipe>> {
        return try {
            val newRecipes = withContext(Dispatchers.IO) { personalRecipeDao.getAll() }
            ResponseHandler(success = newRecipes)
        } catch (e: Exception) {
            Log.e("PersonalRecipeRepository", "getAll(): $e")
            ResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }

    }
    override suspend fun getById(id: Int): ResponseHandler<PersonalRecipe?> {
        return try {
            val newRecipe = withContext(Dispatchers.IO) { personalRecipeDao.getRecipeById(id) }
            ResponseHandler(success = newRecipe)
        } catch (e: Exception) {
            Log.e("PersonalRecipeRepository", "getById(): $e")
            ResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }
    }

    // Data management functions
    override suspend fun add(r: PersonalRecipe): ResponseHandler<PersonalRecipe?> {
        return try {
            personalRecipeDao.insertRecipe(r)
            ResponseHandler() // No need for return
        } catch (e: Exception) {
            Log.e("PersonalRecipeRepository", "add(): $e")
            ResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }
    }
    override suspend fun delete(r: PersonalRecipe): ResponseHandler<PersonalRecipe?> {
        return try {
            personalRecipeDao.deleteRecipe(r)
            ResponseHandler() // No need for return
        } catch (e: Exception) {
            Log.e("PersonalRecipeRepository", "delete(): $e")
            ResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }
    }
    suspend fun update(r: PersonalRecipe): ResponseHandler<PersonalRecipe?> {
        return try {
            personalRecipeDao.updateRecipe(r)
            ResponseHandler()
        } catch (e: Exception) {
            Log.e("PersonalRecipeRepository", "update(): $e")
            ResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }
    }

    // Utility functions
    suspend fun isRecipeInDatabase(id: Int): ResponseHandler<Boolean> {
        return try {
            val recipeFound = personalRecipeDao.getRecipeById(id) != null
            ResponseHandler(success = recipeFound)
        } catch (e: Exception) {
            Log.e("PersonalRecipeRepository", "isRecipeInDatabase(): $e")
            ResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }
    }
}
