package com.example.recipeapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.BuildConfig
import com.example.recipeapp.models.Auth
import com.example.recipeapp.models.User
import com.example.recipeapp.repositories.AuthRepository
import com.example.recipeapp.utils.SharedPreferencesManager
import kotlinx.coroutines.launch

class AuthViewModel(application: Application): BaseViewModel(application) {
    private val repository = AuthRepository()

    private var _user: MutableState<User?> = mutableStateOf(null)
    val user get() = _user.value

    init {
        setLoading(true)
        Log.d("AuthViewModel", "Base url: ${BuildConfig.BASE_URL}")
    }

    // TODO: Add check that the auth token isn't expired
    fun checkAuthToken(): Boolean {
        val token = SharedPreferencesManager.getAuthToken(encryptedSharedPreferences)
        val user = SharedPreferencesManager.getUser(encryptedSharedPreferences)

        Log.d("AUTH", "Token found: $token")
        Log.d("AUTH", "User found: $user")

        setLoading(false)

        return token != null && user != null
    }

    fun register(auth: Auth, callback: () -> Unit) {
        setLoading(true)
        viewModelScope.launch {
            // Calling the api for auth token
            val response = repository.register(auth)
            if (response.isSuccessful() && response.value != null) {
                Log.d("RegisterResponse", response.value.toString())

                // Saving the token to SharedPrefs
                SharedPreferencesManager.saveAuthToken(
                    encryptedSharedPreferences,
                    response.value.token,
                )

                // Saving the user to SharedPrefs
                val user = User(response.value.userId, response.value.userEmail)
                SharedPreferencesManager.saveUser(encryptedSharedPreferences, user)
                callback()
            } else {
                Log.d("AuthViewModel", "Ongelma rekisteröitymisessä: ${response.message} (${response.errorCode}).")
                showAlert("Error occurred in registering (${response.errorCode}).")
            }
            setLoading(false)
        }
    }

    fun login(auth: Auth, callback: () -> Unit) {
        setLoading(true)
        viewModelScope.launch {
            // Calling the api for auth token
            val response = repository.login(auth)
            if (response.isSuccessful() && response.value != null) {
                Log.d("RegisterResponse", response.value.toString())

                // Saving the token to SharedPrefs
                SharedPreferencesManager.saveAuthToken(
                    encryptedSharedPreferences,
                    response.value.token,
                )

                // Saving the user to SharedPrefs
                val user = User(response.value.userId, response.value.userEmail)

                Log.d("AuthViewModel", "Kirjauduttu sisään käyttäjällä: $user")

                SharedPreferencesManager.saveUser(encryptedSharedPreferences, user)
                callback()
            } else {
                Log.d("AuthViewModel", "Ongelma kirjautumisessa: ${response.message} (${response.errorCode}).")
                showAlert("Error occurred in loggin in (${response.errorCode}).")
            }
            setLoading(false)
        }
    }

    fun logout(callback: (() -> Unit)? = null) {
        SharedPreferencesManager.clearPrefs(encryptedSharedPreferences)
        if (callback !== null) callback()
    }
}