package com.example.recipeapp.repositories.room

import android.content.SharedPreferences
import com.example.recipeapp.BuildConfig
import com.example.recipeapp.models.database.MealType
import com.example.recipeapp.models.room.FavouriteRecipe
import com.example.recipeapp.repositories.shared.RepositoryResponseHandler
import com.example.recipeapp.services.database.RetrofitInstance
import com.example.recipeapp.utils.SharedPreferencesManager

/**
 * Repository class for managing today's specials.
 *
 * @param prefs The [android.content.SharedPreferences] instance used for storing and retrieving today's specials.
 */
class TodaysSpecialsRepository(private val prefs: SharedPreferences) {
    /**
     * Retrieves today's specials from SharedPreferences if available, otherwise fetches them from the server.
     *
     * @return A [RepositoryResponseHandler] containing either the list of today's specials on success
     * or an error message on failure.
     */
    suspend fun getTodaysSpecials(): RepositoryResponseHandler<List<FavouriteRecipe>> {
        val isLoaded = SharedPreferencesManager.isTodaysSpecialsLoaded(prefs)
        if (isLoaded) {
            val newRecipes = SharedPreferencesManager.getTodaysSpecials(prefs)
            return if (newRecipes != null) {
                RepositoryResponseHandler(success = newRecipes)
            } else {
                RepositoryResponseHandler(error = "Error with getting saved specials!")
            }
        } else {
            val newRecipes = MealType.entries.map {
                val response = RetrofitInstance().recipeService.getRandomRecipes(BuildConfig.API_KEY, it.toString().lowercase(), 1)
                if (response.isSuccessful) {
                    response.body()?.recipes?.get(0)?.toSavable()
                } else {
                    return RepositoryResponseHandler(error = "Error with the code of ${response.code()} occurred!")
                }
            }

            SharedPreferencesManager.saveTodaysSpecials(prefs, newRecipes.filterNotNull())
            return RepositoryResponseHandler(success = newRecipes.filterNotNull())
        }
    }
}