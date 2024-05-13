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

        SharedPreferencesManager.saveTodaysSpecial(context, recipe, mealType)
        return recipe
    }

    suspend fun fetchRandomRecipes(context: Context) {
        try {
            _recipes.clear()
            if (SharedPreferencesManager.isTodaysSpecialsLoaded(context)) {
                val newRecipes: List<Recipe> = SharedPreferencesManager.getTodaysSpecials(context)
                _recipes.addAll(newRecipes)
            } else {
                SharedPreferencesManager.TODAYS_SPECIAL_MEAL_TYPES.map {
                    val recipe: Recipe = fetchRandomMeal(context, it)
                    _recipes.add(recipe)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun fetchRecipeByUri(context: Context, uri: String): Recipe {
        return RetrofitInstance().recipeService.getRecipeByUri(
            type = "public",
            uri = uri,
            appId = APP_ID,
            appKey = APP_KEY,
        ).hits[0].recipe
    }
}