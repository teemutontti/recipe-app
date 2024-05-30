package com.example.recipeapp.repositories

import android.util.Log
import com.example.recipeapp.models.Recipe
import com.example.recipeapp.models.room.AppDatabase
import com.example.recipeapp.models.room.FavouriteRecipe
import com.example.recipeapp.models.room.FavouriteRecipeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavouriteRecipeRepository(db: AppDatabase): ManageableRecipeRepository<FavouriteRecipe> {
    private var favouriteRecipeDao: FavouriteRecipeDao = db.favouriteRecipeDao()

    // Data fetching functions
    override suspend fun getAll(): ResponseHandler<List<FavouriteRecipe>> {
        return try {
            val newRecipes = withContext(Dispatchers.IO) { favouriteRecipeDao.getAll() }
            ResponseHandler(success = newRecipes)
        } catch (e: Exception) {
            Log.e("FavouriteRecipeRepository", "getAll(): $e")
            ResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }
    }
    override suspend fun getById(id: Int): ResponseHandler<FavouriteRecipe?> {
        return try {
            val newRecipe = withContext(Dispatchers.IO) { favouriteRecipeDao.getRecipeById(id) }
            ResponseHandler(success = newRecipe)
        } catch (e: Exception) {
            Log.e("FavouriteRecipeRepository", "getById(): $e")
            ResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }
    }

    // Data management functions
    override suspend fun add(r: FavouriteRecipe): ResponseHandler<FavouriteRecipe?> {
        return try {
            favouriteRecipeDao.insertRecipe(r)
            ResponseHandler()
        } catch (e: Exception) {
            Log.e("FavouriteRecipeRepository", "add(): $e")
            ResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }
    }
    override suspend fun delete(r: FavouriteRecipe): ResponseHandler<FavouriteRecipe?> {
        return try {
            favouriteRecipeDao.deleteRecipe(r)
            ResponseHandler()
        } catch (e: Exception) {
            Log.e("FavouriteRecipeRepository", "delete(): $e")
            ResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }
    }
}
