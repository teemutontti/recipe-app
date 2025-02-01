package com.example.recipeapp.repositories

import android.content.SharedPreferences
import com.example.recipeapp.models.Log
import com.example.recipeapp.services.RetrofitInstance
import com.example.recipeapp.utils.Result
import com.example.recipeapp.utils.SharedPreferencesManager
import java.time.LocalDate

class LogRepository(private val encryptedPrefs: SharedPreferences) {
    private val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)
    private val userId: Int? = SharedPreferencesManager.getUser(encryptedPrefs)?.id
    private val retrofitInstance = RetrofitInstance()
    private val service = retrofitInstance.logsService

    suspend fun getLogsByDate(date: LocalDate): Result<List<Log>> {
        return try {
            android.util.Log.d("LogRepository", "Haetaan avaimella: $token")
            android.util.Log.d("LogRepository", "Haetaan käyttäjällä: ${SharedPreferencesManager.getUser(encryptedPrefs)}")
            val response = service.getLogsByDateAndUser(
                date.toString(),
                userId ?: -1,
                "Bearer $token"
            )
            if (response.isSuccessful && response.body() != null) Result.success(response.body())
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun saveLog(log: Log): Result<Boolean> {
        return try {
            val response = service.saveLog(log, "Bearer $token")
            if (response.isSuccessful) Result.success(true)
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun deleteLog(id: Int): Result<Boolean> {
        return try {
            val response = service.deleteLog(id, "Bearer $token")
            if (response.isSuccessful) Result.success(true)
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun updateLog(log: Log): Result<Log> {
        return try {
            if (log.id != null) {
                val response = service.updateLog(log.id, log, "Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body())
                } else {
                    Result.fail(response.code())
                }
            } else {
                Result.fail(400, "Log ID cannot be null.")
            }
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }
}