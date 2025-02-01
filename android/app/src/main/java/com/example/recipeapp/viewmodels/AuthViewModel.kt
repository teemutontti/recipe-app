package com.example.recipeapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.models.Auth
import com.example.recipeapp.models.User
import com.example.recipeapp.repositories.AuthRepository
import com.example.recipeapp.utils.SharedPreferencesManager
import kotlinx.coroutines.launch

class AuthViewModel(application: Application): BaseViewModel(application) {
    private val repository = AuthRepository()

    init {
        setLoading(true)
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
                SharedPreferencesManager.saveUser(encryptedSharedPreferences, user)
                callback()
            }
            setLoading(false)
        }
    }

    fun logout() {
        SharedPreferencesManager.clearPrefs(encryptedSharedPreferences)
    }
}