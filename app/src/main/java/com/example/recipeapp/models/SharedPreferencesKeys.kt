package com.example.recipeapp.models

/**
 * Object containing keys for SharedPreferences used in the Recipe App.
 */
object SharedPreferencesKeys {
    // The name of the SharedPreferences file
    const val TODAYS_SPECIALS = "todays_specials"

    // Key for storing the last load date of today's specials.
    const val PREFS_NAME = "recipe_prefs"

    // Key for storing today's specials in SharedPreferences.
    const val TODAYS_SPECIALS_LAST_LOAD = "todays_specials_load_date"
}
