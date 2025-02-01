package com.example.recipeapp.viewmodels

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.recipeapp.repositories.AuthRepository
import com.example.recipeapp.utils.Alert
import com.example.recipeapp.utils.AlertType
import com.example.recipeapp.utils.SharedPreferencesKeys
import com.example.recipeapp.utils.SharedPreferencesManager

open class BaseViewModel(application: Application): AndroidViewModel(application) {
    protected val encryptedSharedPreferences: SharedPreferences;

    init {
        val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        encryptedSharedPreferences = EncryptedSharedPreferences.create(
            SharedPreferencesKeys.SECRET_PREFS_NAME,
            masterKey,
            application.applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val _loading: MutableState<Boolean> = mutableStateOf(false)
    val loading get() = _loading.value
    val setLoading: (Boolean) -> Unit = { _loading.value = it }

    private val _alert: MutableState<Alert?> = mutableStateOf(null)
    val alert get() = _alert.value

    fun showAlert(message: String, type: AlertType? = AlertType.ERROR) {
        _alert.value = Alert(message, type)
    }

    fun getUserId(): Int {
        return SharedPreferencesManager.getUser(encryptedSharedPreferences)?.id ?: -1
    }

    fun clearAlert() {
        _alert.value = null
    }
}