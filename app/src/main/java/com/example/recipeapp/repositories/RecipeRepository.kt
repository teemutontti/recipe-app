package com.example.recipeapp.repositories

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.recipeapp.BuildConfig.API_KEY
import com.example.recipeapp.services.RetrofitInstance
import com.example.recipeapp.utils.SharedPreferencesManager
import com.example.recipeapp.api.Recipe

object RecipeRepository: ViewModel() {
    private val prefs = SharedPreferencesManager
    private var _specials: SnapshotStateList<Recipe> = mutableStateListOf<Recipe>()
    //private var _current: MutableState<Recipe> = mutableStateOf<Recipe>(value )
    val specials: List<Recipe> get() = _specials
    //val current: Recipe get() = _current.value

    private suspend fun fetchRandomMeal(context: Context, mealType: String): Recipe {
        val recipe: Recipe = RetrofitInstance().recipeService.getRandomRecipes(
            apiKey = API_KEY,
            includeTags = mealType,
            number = 1
        ).recipes[0]

        // Saving to shared prefs
        prefs.saveTodaysSpecial(context, recipe, mealType)
        return recipe
    }

    suspend fun fetchRandomRecipes(context: Context) {
        try {
            _specials.clear()
            if (SharedPreferencesManager.isTodaysSpecialsLoaded(context)) {
                val recipe: List<Recipe> = SharedPreferencesManager.getTodaysSpecials(context)
            } else {
                SharedPreferencesManager.TODAYS_SPECIAL_MEAL_TYPES.map {
                    val recipe: Recipe = fetchRandomMeal(context, it)
                    _specials.add(recipe)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun fetchRecipeById(context: Context, id: Int): Recipe? {
        try {
            val recipe = RetrofitInstance().recipeService.getRecipeInformation(
                apiKey = API_KEY,
                id = id
            )
            //_current.value = recipe
            return recipe
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}