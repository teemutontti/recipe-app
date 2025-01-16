package com.example.recipeapp.viewmodels

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.example.recipeapp.utils.Alert
import com.example.recipeapp.utils.AlertType

open class BaseViewModel(application: Application): AndroidViewModel(application) {
    private val _loading: MutableState<Boolean> = mutableStateOf(false)
    val loading get() = _loading.value
    val setLoading: (Boolean) -> Unit = { _loading.value = it }

    private val _alert: MutableState<Alert?> = mutableStateOf(null)
    val alert get() = _alert.value

    fun showAlert(message: String, type: AlertType? = AlertType.ERROR) {
        _alert.value = Alert(message, type)
    }

    fun clearAlert() {
        _alert.value = null
    }
}