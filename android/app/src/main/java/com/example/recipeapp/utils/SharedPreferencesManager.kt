package com.example.recipeapp.models

import android.content.SharedPreferences
import com.example.recipeapp.utils.SharedPreferencesKeys.SHOPPING_LIST
import com.example.recipeapp.utils.SharedPreferencesKeys.TODAYS_SPECIALS
import com.example.recipeapp.utils.SharedPreferencesKeys.TODAYS_SPECIALS_LAST_LOAD
import com.google.gson.Gson
import java.time.LocalDate

/**
 * Object responsible for managing SharedPreferences related to the Recipe App.
 */
object SharedPreferencesManager {
    /**
     * Checks if today's specials have been loaded based on the last load date stored in SharedPreferences.
     *
     * @param prefs SharedPreferences instance to retrieve the last load date.
     * @return true if today's specials are loaded; false otherwise.
     */
    fun isTodaysSpecialsLoaded(prefs: SharedPreferences): Boolean {
        val currentDate = LocalDate.now().toString() // MinSDK raised to 26 to use LocalDate.now()
        return prefs.getString(TODAYS_SPECIALS_LAST_LOAD, null) == currentDate
    }

    /**
     * Saves today's specials and the current date of load into SharedPreferences.
     *
     * @param prefs SharedPreferences instance to store the data.
     * @param recipes List of today's special recipes to be saved.
     */
    fun saveTodaysSpecials(prefs: SharedPreferences, recipes: List<FavouriteRecipe>) {
        prefs.edit().putString(TODAYS_SPECIALS, Gson().toJson(recipes)).apply()
        prefs.edit().putString(TODAYS_SPECIALS_LAST_LOAD, LocalDate.now().toString()).apply()
    }

    /**
     * Retrieves today's specials from SharedPreferences.
     *
     * @param prefs SharedPreferences instance to retrieve the data.
     * @return List of today's special recipes if available; null otherwise.
     */
    fun getTodaysSpecials(prefs: SharedPreferences): List<FavouriteRecipe>? {
        val json: String? = prefs.getString(TODAYS_SPECIALS, null)
        if (json != null) {
            return Gson().fromJson(json, Array<FavouriteRecipe>::class.java).toList()
        }
        return null
    }

    fun getShoppingList(prefs: SharedPreferences): List<ShoppingListItem>? {
        val json: String? = prefs.getString(SHOPPING_LIST, null)
        if (json != null) {
            return Gson().fromJson(json, Array<ShoppingListItem>::class.java).toList()
        }
        return null
    }

    fun saveShoppingList(prefs: SharedPreferences, ingredients: List<ShoppingListItem>) {
        prefs.edit().putString(SHOPPING_LIST, Gson().toJson(ingredients)).apply()
    }
}
