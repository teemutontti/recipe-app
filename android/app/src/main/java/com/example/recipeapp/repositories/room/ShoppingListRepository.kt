package com.example.recipeapp.repositories.room

import android.content.SharedPreferences
import com.example.recipeapp.utils.SharedPreferencesManager
import com.example.recipeapp.models.room.ShoppingListItem
import com.example.recipeapp.repositories.shared.RepositoryResponseHandler

class ShoppingListRepository(private val prefs: SharedPreferences) {
    fun getShoppingList(): RepositoryResponseHandler<List<ShoppingListItem>> {
        val newShoppingList = SharedPreferencesManager.getShoppingList(prefs)
        return RepositoryResponseHandler(success = newShoppingList ?: listOf())
    }

    fun setShoppingList(newShoppingList: List<ShoppingListItem>) {
        SharedPreferencesManager.saveShoppingList(prefs, newShoppingList)
    }
}