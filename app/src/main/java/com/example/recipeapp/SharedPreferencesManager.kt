package com.example.recipeapp

import android.content.Context
import android.content.SharedPreferences
import android.health.connect.datatypes.MealType
import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import java.time.LocalDate

object SharedPreferencesManager {
    val TODAYS_SPECIAL_MEAL_TYPES = listOf("breakfast", "lunch", "dinner")
    private const val PREFS_NAME = "recipe_prefs"

    fun isTodaysSpecialsLoaded(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val calendar = Calendar.getInstance()
        Log.d("SharedPreferences", Calendar.YEAR.toString())

        return true
    }

    fun saveTodaysSpecial(context: Context, recipe: Recipe, mealType: String) {
        Log.d("RecipeRepository", "Saving recipe to shared prefs")

        val prefs = context.getSharedPreferences("recipe_prefs", Context.MODE_PRIVATE)
        val prefEditor: SharedPreferences.Editor = prefs.edit()

        prefEditor.putString("todays_${mealType}_special", Gson().toJson(recipe))
        //prefEditor.putBoolean("is_daily_recipe_loaded", true)

        prefEditor.commit()
    }

    fun getTodaysSpecials(context: Context): List<Recipe> {
        Log.d("RecipeRepository", "Getting todays specials from shared prefs")

        val prefs = context.getSharedPreferences("recipe_prefs", Context.MODE_PRIVATE)

        return TODAYS_SPECIAL_MEAL_TYPES.map {
            Gson().fromJson(
                prefs.getString("todays_${it}_special",null),
                Recipe::class.java
            )
        }
    }
}