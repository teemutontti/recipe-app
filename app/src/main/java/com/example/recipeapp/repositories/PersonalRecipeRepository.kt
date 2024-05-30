package com.example.recipeapp.repositories

import com.example.recipeapp.models.room.PersonalRecipe
import com.example.recipeapp.models.room.AppDatabase
import com.example.recipeapp.models.room.PersonalRecipeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersonalRecipeRepository(db: AppDatabase): ManageableRecipeRepository<PersonalRecipe> {
    private val personalRecipeDao: PersonalRecipeDao = db.personalRecipeDao()

    // Data fetching functions
    override suspend fun getAll(): List<PersonalRecipe> {
        return withContext(Dispatchers.IO) {
            personalRecipeDao.getAll()
        }
    }
    override suspend fun getById(id: Int): PersonalRecipe? {
        return withContext(Dispatchers.IO) {
            personalRecipeDao.getRecipeById(id)
        }
    }

    // Data management functions
    override suspend fun add(r: PersonalRecipe) = personalRecipeDao.insertRecipe(r)
    override suspend fun update(r: PersonalRecipe) = personalRecipeDao.updateRecipe(r)
    override suspend fun delete(r: PersonalRecipe) = personalRecipeDao.deleteRecipe(r)

    // Utility functions
    suspend fun isRecipeInDatabase(id: Int) = personalRecipeDao.getRecipeById(id) != null
}
