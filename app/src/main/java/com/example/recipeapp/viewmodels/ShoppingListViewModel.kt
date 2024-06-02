package com.example.recipeapp.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import com.example.recipeapp.models.Ingredient
import com.example.recipeapp.models.ShoppingListItem
import com.example.recipeapp.repositories.ShoppingListRepository
import com.example.recipeapp.utils.SharedPreferencesKeys.PREFS_NAME

class ShoppingListViewModel(application: Application): AndroidViewModel(application) {
    private val prefs: SharedPreferences
    private val repository: ShoppingListRepository
    private val _items = mutableStateListOf<ShoppingListItem>()

    init {
        val context = application.applicationContext
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        repository = ShoppingListRepository(prefs)
        loadData()
    }
    val items: List<ShoppingListItem> get() = _items

    private fun loadData() {
        val handler = repository.getShoppingList()
        if (handler.success != null) {
            _items.clear()
            _items.addAll(handler.success)
        }
    }

    fun add(i: ShoppingListItem) {
        val newShoppingList = _items.toMutableList()
        newShoppingList.add(i)
        repository.setShoppingList(newShoppingList)
        loadData()
    }

    fun delete(ingredientIndex: Int) {
        val newShoppingList = _items.toMutableList()
        newShoppingList.removeAt(ingredientIndex)
        repository.setShoppingList(newShoppingList)
        loadData()
    }


}