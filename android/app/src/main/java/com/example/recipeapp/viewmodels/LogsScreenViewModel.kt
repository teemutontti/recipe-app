package com.example.recipeapp.viewmodels

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.models.Food
import com.example.recipeapp.models.FoodLog
import com.example.recipeapp.models.Log
import com.example.recipeapp.models.MealType
import com.example.recipeapp.models.NutrientSummary
import com.example.recipeapp.repositories.FoodRepository
import com.example.recipeapp.repositories.LogRepository
import com.example.recipeapp.utils.Constants
import com.example.recipeapp.utils.ConversionUtils.emptyFood
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class LogsScreenViewModel(application: Application): AndroidViewModel(application) {
    private val logRepository = LogRepository()
    private val foodRepository = FoodRepository()

    private var _logs: MutableState<List<Log>> = mutableStateOf(emptyList())
    val logs get() = _logs.value
    val setLogs: (List<Log>) -> Unit = { _logs.value = it }

    private var _breakfastLogs: MutableState<List<FoodLog>> = mutableStateOf(emptyList())
    val breakfastLogs get() = _breakfastLogs.value
    private var _lunchLogs: MutableState<List<FoodLog>> = mutableStateOf(emptyList())
    val lunchLogs get() = _lunchLogs.value
    private var _dinnerLogs: MutableState<List<FoodLog>> = mutableStateOf(emptyList())
    val dinnerLogs get() = _dinnerLogs.value
    private var _snacksLogs: MutableState<List<FoodLog>> = mutableStateOf(emptyList())
    val snacksLogs get() = _snacksLogs.value

    private var _foods: MutableState<List<Food>> = mutableStateOf(emptyList())
    val foods get() = _foods.value
    val setFoods: (List<Food>) -> Unit = { _foods.value = it }

    private var _date: MutableState<LocalDate> = mutableStateOf(LocalDate.now())
    val date get() = _date.value
    val setDate: (LocalDate) -> Unit = { _date.value = it; loadLogs() }

    private var _savableFood: MutableState<Food?> = mutableStateOf(null)
    val savableFood get() = _savableFood.value
    val setSavableFood: (Food?) -> Unit = { _savableFood.value = it }

    private var _editableLogs: MutableState<List<Log>> = mutableStateOf(emptyList())
    val editableLogs get() = _editableLogs.value
    val setEditableLogs: (List<Log>) -> Unit = { _editableLogs.value = it }

    private var _overallNutrients = mutableStateOf(NutrientSummary(0.0, 0.0, 0.0, 0.0))
    val overallNutrients get() = _overallNutrients.value
    val setOverallNutrients: (NutrientSummary) -> Unit = { _overallNutrients.value = it }

    private var _mealNutrients = mutableStateOf<Map<MealType, NutrientSummary>>(emptyMap())
    val nutrients get() = _mealNutrients.value
    val setNutrients: (Map<MealType, NutrientSummary>) -> Unit = { _mealNutrients.value = it }

    private suspend fun calculateNutrients(logs: List<Log>): NutrientSummary {
        var calories = 0.0
        var fats = 0.0
        var carbs = 0.0
        var protein = 0.0

        logs.forEach { log ->
            val food = fetchFoodById(log.food)
            food?.calories?.let { calories += it * (log.amount / 100) }
            food?.fat?.let { fats += it * (log.amount / 100) }
            food?.carbs?.let { carbs += it * (log.amount / 100) }
            food?.protein?.let { protein += it * (log.amount / 100) }
        }

        return NutrientSummary(calories, fats, carbs, protein)
    }

    fun loadLogs() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = logRepository.getLogsByDate(_date.value)
            _logs.value = result ?: emptyList()

            val nutrientSummary = result?.let { calculateNutrients(it) }
            if (nutrientSummary != null) setOverallNutrients(nutrientSummary)

            val newBreakfastLogs = result?.filter { it.meal == "BREAKFAST" } ?: emptyList()
            val newLunchLogs = result?.filter { it.meal == "LUNCH" } ?: emptyList()
            val newDinnerLogs = result?.filter { it.meal == "DINNER" } ?: emptyList()
            val newSnacksLogs = result?.filter {
                it.meal == "SNACKS" || (it.meal != "BREAKFAST"
                        && it.meal != "LUNCH"
                        && it.meal != "DINNER")} ?: emptyList()

            _breakfastLogs.value = newBreakfastLogs.map {
                FoodLog(it, fetchFoodById(it.food) ?: emptyFood.copy())
            }
            _lunchLogs.value = newLunchLogs.map {
                FoodLog(it, fetchFoodById(it.food) ?: emptyFood.copy())
            }
            _dinnerLogs.value = newDinnerLogs.map {
                FoodLog(it, fetchFoodById(it.food) ?: emptyFood.copy())
            }
            _snacksLogs.value = newSnacksLogs.map {
                FoodLog(it, fetchFoodById(it.food) ?: emptyFood.copy())
            }

            val breakfastNutrients = calculateNutrients(newBreakfastLogs)
            val lunchNutrients = calculateNutrients(newLunchLogs)
            val dinnerNutrients = calculateNutrients(newDinnerLogs)
            val snacksNutrients = calculateNutrients(newSnacksLogs)

            val newMealNutrients = MealType.entries.associateWith { mealType ->
                when (mealType) {
                    MealType.BREAKFAST -> breakfastNutrients
                    MealType.LUNCH -> lunchNutrients
                    MealType.DINNER -> dinnerNutrients
                    MealType.SNACKS -> snacksNutrients
                }
            }
            android.util.Log.d("LogsScreenViewModel", "newBreakfastLogs: $newMealNutrients")
            setNutrients(newMealNutrients)
        }
    }

    fun loadFoods() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = foodRepository.getFoods()
            _foods.value = result ?: emptyList()
        }
    }

    fun updateLog(log: Log) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = logRepository.updateLog(log)
            if (result != null) _logs.value = _logs.value.map {
                if (it.id == log.id) log
                else it
            }
        }
    }

    fun saveLog(log: Log) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = logRepository.saveLog(log)
            if (result) android.util.Log.d("LogScreenViewModel", "Log saved")
            else android.util.Log.d("LogScreenViewModel", "Log NOT saved")
        }
    }

    fun deleteLog(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = logRepository.deleteLog(id)
            if (result) {
                _logs.value = _logs.value.filter { id != it.id }
                android.util.Log.d("LogScreenViewModel", "Log deleted")
            } else {
                android.util.Log.d("LogScreenViewModel", "Log NOT deleted")
            }
        }
    }

    fun searchFoods(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = foodRepository.getFoodsByQuery(query)
            _foods.value = result ?: emptyList()
        }
    }

    suspend fun fetchFoodById(id: Int): Food?  {
        return foodRepository.getFoodById(id)
    }

    fun saveFood(food: Food) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = foodRepository.saveFood(food)
            if (result) {
                loadFoods()
                android.util.Log.d("LogScreenViewModel", "Food saved")
            } else {
                android.util.Log.d("LogScreenViewModel", "Food NOT saved")
            }
        }
    }
}