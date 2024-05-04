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

private object RecipeStorage {
    fun isDailyRecipeLoaded(context: Context): Boolean {
        val prefs = context.getSharedPreferences("recipe_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("is_daily_recipe_loaded", false)
    }

    fun saveDailyRecipe(context: Context, recipe: Recipe, mealType: String) {
        Log.d("RecipeRepository", "Saving recipe to shared prefs")

        val prefs = context.getSharedPreferences("recipe_prefs", Context.MODE_PRIVATE)
        val prefEditor: SharedPreferences.Editor = prefs.edit()

        prefEditor.putString("daily_random_$mealType", Gson().toJson(recipe))
        prefEditor.putBoolean("is_daily_recipe_loaded", true)

        prefEditor.commit()
    }

    fun getDailyRecipes(context: Context): List<Recipe> {
        Log.d("RecipeRepository", "Getting recipes from shared prefs")

        val prefs = context.getSharedPreferences("recipe_prefs", Context.MODE_PRIVATE)

        return DAILY_RANDOM_MEAL_TYPES.map {
            Gson().fromJson(
                prefs.getString("daily_random_$it",null),
                Recipe::class.java
            )
        }
    }
}

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