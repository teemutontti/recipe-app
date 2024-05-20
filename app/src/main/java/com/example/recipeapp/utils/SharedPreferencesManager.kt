package com.example.recipeapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.recipeapp.api.CachedRecipe
import com.example.recipeapp.api.Recipe
import com.google.gson.Gson
import java.time.LocalDate

object SharedPreferencesManager {
    private const val PREFS_NAME = "recipe_prefs"
    private const val TODAYS_SPECIALS_LAST_LOAD_KEY = "todays_specials_load_date"
    private const val TODAYS_SPECIALS_KEY = "todays_specials"
    private const val FAVOURITES_KEY = "favourite_recipes"
    private const val OWN_RECIPES_KEY = "own_recipes"

    fun isTodaysSpecialsLoaded(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // MinSDK raised to 26 from 24 because of LocalDate.now()
        val currentDate = LocalDate.now().toString()

        return prefs.getString(TODAYS_SPECIALS_LAST_LOAD_KEY, null) == currentDate
    }

    fun saveTodaysSpecials(context: Context, recipes: List<CachedRecipe?>) {
        Log.d("RecipeRepository", "Saving recipe to shared prefs")

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val prefEditor: SharedPreferences.Editor = prefs.edit()

        prefEditor.putString(TODAYS_SPECIALS_KEY, Gson().toJson(recipes))
        prefEditor.putString(TODAYS_SPECIALS_LAST_LOAD_KEY, LocalDate.now().toString())

        prefEditor.commit()
    }

    fun getTodaysSpecials(context: Context): List<CachedRecipe> {
        Log.d("RecipeRepository", "Getting todays specials from shared prefs")

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val json: String = prefs.getString(TODAYS_SPECIALS_KEY, null) ?: return listOf()
        val specials = Gson().fromJson(json, Array<CachedRecipe>::class.java).toList()

        Log.d("SharedPrefs", specials.toString())

        return specials
    }

    fun getFavourites(context: Context): List<CachedRecipe> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonFavourites = prefs.getString(FAVOURITES_KEY, null) ?: return listOf()
        val favourites = Gson().fromJson(jsonFavourites, Array<CachedRecipe>::class.java).toList()

        Log.d("SharedPrefs", "Favourites: $favourites")
        return favourites
    }

    fun updateFavourites(context: Context, newFavourites: List<CachedRecipe>) {
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