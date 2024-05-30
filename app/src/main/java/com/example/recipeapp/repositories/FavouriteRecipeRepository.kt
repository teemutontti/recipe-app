package com.example.recipeapp.repositories

import com.example.recipeapp.models.Recipe
import com.example.recipeapp.models.room.AppDatabase
import com.example.recipeapp.models.room.FavouriteRecipe
import com.example.recipeapp.models.room.FavouriteRecipeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavouriteRecipeRepository(db: AppDatabase): ManageableRecipeRepository<FavouriteRecipe> {
    private var favouriteRecipeDao: FavouriteRecipeDao = db.favouriteRecipeDao()

    // Data fetching functions
    override suspend fun getAll(): List<FavouriteRecipe> {
        return withContext(Dispatchers.IO) {
            favouriteRecipeDao.getAll()
        }
    }
    override suspend fun getById(id: Int): FavouriteRecipe? {
        return withContext(Dispatchers.IO) {
            favouriteRecipeDao.getRecipeById(id)
        }
    }

    // Data management functions
    override suspend fun add(r: FavouriteRecipe) = favouriteRecipeDao.insertRecipe(r)
    override suspend fun delete(r: FavouriteRecipe) = favouriteRecipeDao.deleteRecipe(r)
}
