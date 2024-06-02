package com.example.recipeapp.repositories

import android.content.SharedPreferences
import com.example.recipeapp.models.Ingredient
import com.example.recipeapp.models.SharedPreferencesManager
import com.example.recipeapp.models.ShoppingListItem

class ShoppingListRepository(private val prefs: SharedPreferences) {
    fun getShoppingList(): RepositoryResponseHandler<List<ShoppingListItem>> {
        val newShoppingList = SharedPreferencesManager.getShoppingList(prefs)
        return RepositoryResponseHandler(success = newShoppingList ?: listOf())
    }

    fun setShoppingList(newShoppingList: List<ShoppingListItem>) {
        SharedPreferencesManager.saveShoppingList(prefs, newShoppingList)
    }
}