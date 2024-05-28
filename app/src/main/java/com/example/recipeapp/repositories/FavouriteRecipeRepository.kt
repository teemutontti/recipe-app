package com.example.recipeapp.repositories

import com.example.recipeapp.models.CachedRecipe
import com.example.recipeapp.models.room.AppDatabase
import com.example.recipeapp.models.room.FavouriteRecipe
import com.example.recipeapp.models.room.FavouriteRecipeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavouriteRecipeRepository(db: AppDatabase) {
    private var favouriteRecipeDao: FavouriteRecipeDao = db.favouriteRecipeDao()

    suspend fun getAllRecipes(): List<FavouriteRecipe> {
        return withContext(Dispatchers.IO) {
            favouriteRecipeDao.getAll()
        }
    }

    suspend fun add(recipe: FavouriteRecipe) {
        favouriteRecipeDao.insertRecipe(recipe)
    }

    suspend fun update(recipe: FavouriteRecipe) {
        favouriteRecipeDao.updateRecipe(recipe)
    }

    suspend fun delete(recipe: FavouriteRecipe) {
        favouriteRecipeDao.deleteRecipe(recipe)
    }
}