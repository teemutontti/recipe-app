package com.example.recipeapp

import android.content.Context
import android.content.SharedPreferences
import android.health.connect.datatypes.MealType
import java.util.Calendar
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import java.time.LocalDate

object SharedPreferencesManager {
    val TODAYS_SPECIAL_MEAL_TYPES = listOf("breakfast", "lunch", "dinner")
    private const val PREFS_NAME = "recipe_prefs"
    private const val TODAYS_SPECIALS_LAST_LOAD_KEY = "todays_specials_load_date"

    fun isTodaysSpecialsLoaded(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // MinSDK raised to 26 from 24 because of LocalDate.now()
        val currentDate = LocalDate.now().toString()

        return prefs.getString(TODAYS_SPECIALS_LAST_LOAD_KEY, null) == currentDate
    }

    fun saveTodaysSpecial(context: Context, recipe: Recipe, mealType: String) {
        Log.d("RecipeRepository", "Saving recipe to shared prefs")

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val prefEditor: SharedPreferences.Editor = prefs.edit()

        prefEditor.putString("todays_${mealType}_special", Gson().toJson(recipe))
        prefEditor.putString(TODAYS_SPECIALS_LAST_LOAD_KEY, LocalDate.now().toString())

        prefEditor.commit()
    }

    fun getTodaysSpecials(context: Context): List<Recipe> {
        Log.d("RecipeRepository", "Getting todays specials from shared prefs")

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        return TODAYS_SPECIAL_MEAL_TYPES.map {
            val recipeJson: String = prefs.getString("todays_${it}_special", null) ?: return listOf()
            Gson().fromJson(recipeJson, Recipe::class.java)
        }
    }
}