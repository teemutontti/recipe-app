package com.example.recipeapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.recipeapp.api.Recipe
import com.google.gson.Gson
import java.time.LocalDate

object SharedPreferencesManager {
    val TODAYS_SPECIAL_MEAL_TYPES = listOf("breakfast", "lunch", "dinner")
    private const val PREFS_NAME = "recipe_prefs"
    private const val TODAYS_SPECIALS_LAST_LOAD_KEY = "todays_specials_load_date"
    private const val FAVOURITES_KEY = "favourite_recipes"
    private const val OWN_RECIPES_KEY = "own_recipes"

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

        val specials: List<Recipe> = TODAYS_SPECIAL_MEAL_TYPES.map {
            val recipeJson: String = prefs.getString("todays_${it}_special", null) ?: return listOf()
            Gson().fromJson(recipeJson, Recipe::class.java)
        }
        Log.d("SharedPrefs", specials.toString())

        return specials
    }

    fun getFavourites(context: Context): List<Recipe> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonFavourites = prefs.getString(FAVOURITES_KEY, null) ?: return listOf()
        val favourites = Gson().fromJson(jsonFavourites, Array<Recipe>::class.java).toList()

        Log.d("SharedPrefs", "Favourites: $favourites")
        return favourites
    }

    fun updateFavourites(context: Context, newFavourites: List<Recipe>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val prefEditor: SharedPreferences.Editor = prefs.edit()

        prefEditor.putString(FAVOURITES_KEY, Gson().toJson(newFavourites))

        prefEditor.commit()
    }

    fun getOwnRecipes(context: Context): List<Recipe> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(OWN_RECIPES_KEY, null) ?: return listOf()
        val ownRecipes = Gson().fromJson(json, Array<Recipe>::class.java).toList()

        Log.d("SharedPrefs", "Own recipes: $ownRecipes")
        return ownRecipes
    }

    fun updateOwnRecipe(context: Context, newOwnRecipe: List<Recipe>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val prefEditor: SharedPreferences.Editor = prefs.edit()

        prefEditor.putString(OWN_RECIPES_KEY, Gson().toJson(newOwnRecipe))
        prefEditor.commit()
    }
}