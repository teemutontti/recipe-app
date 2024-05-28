package com.example.recipeapp.models

import android.content.SharedPreferences
import com.example.recipeapp.models.SharedPreferencesKeys.TODAYS_SPECIALS
import com.example.recipeapp.models.SharedPreferencesKeys.TODAYS_SPECIALS_LAST_LOAD
import com.google.gson.Gson
import java.time.LocalDate

object SharedPreferencesManager {
    fun isTodaysSpecialsLoaded(prefs: SharedPreferences): Boolean {
        val currentDate = LocalDate.now().toString() // MinSDK raised to 26 to use LocalDate.now()
        return prefs.getString(TODAYS_SPECIALS_LAST_LOAD, null) == currentDate
    }

    fun saveTodaysSpecials(prefs: SharedPreferences, recipes: List<CachedRecipe?>) {
        prefs.edit().putString(TODAYS_SPECIALS, Gson().toJson(recipes)).apply()
        prefs.edit().putString(TODAYS_SPECIALS_LAST_LOAD, LocalDate.now().toString()).apply()
    }

    fun getTodaysSpecials(prefs: SharedPreferences): List<CachedRecipe>? {
        val json: String? = prefs.getString(TODAYS_SPECIALS, null)
        if (json != null) {
            return Gson().fromJson(json, Array<CachedRecipe>::class.java).toList()
        }
        return null
    }
}
