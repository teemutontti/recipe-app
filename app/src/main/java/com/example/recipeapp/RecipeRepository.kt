package com.example.recipeapp

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.recipeapp.BuildConfig.APP_ID
import com.example.recipeapp.BuildConfig.APP_KEY
import com.google.gson.Gson
import java.lang.Exception

private val DAILY_RANDOM_MEAL_TYPES = listOf("breakfast", "lunch", "dinner")

object RecipeRepository: ViewModel() {
    private var _recipes: SnapshotStateList<Recipe> = mutableStateListOf<Recipe>()
    val recipes: List<Recipe> get() = _recipes

    private suspend fun fetchRandomMeal(context: Context, mealType: String): Recipe {
        val recipe = RetrofitInstance().recipeService.getRandomRecipes(
            type = "public",
            appId = APP_ID,
            appKey = APP_KEY,
            random = true,
            mealType = mealType
        ).hits[0].recipe

        RecipeStorage.saveDailyRecipe(context, recipe, mealType)
        return recipe
    }

    suspend fun fetchRandomRecipes(context: Context) {
        try {
            _recipes.clear()
            if (RecipeStorage.isDailyRecipeLoaded(context)) {
                val newRecipes: List<Recipe> = RecipeStorage.getDailyRecipes(context)
                _recipes.addAll(newRecipes)
            } else {
                listOf("breakfast", "lunch", "dinner").map {
                    _recipes.add(fetchRandomMeal(context, it))
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}