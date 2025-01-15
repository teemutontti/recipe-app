package com.example.recipeapp.repositories

import com.example.recipeapp.models.Log
import com.example.recipeapp.services.RetrofitInstance
import java.time.LocalDate

class LogRepository {
    private val retrofitInstance = RetrofitInstance()
    private val service = retrofitInstance.logsService

    suspend fun getLogsByDate(date: LocalDate): List<Log>? {
        return try {
            val response = service.getLogsByDate(date.toString())
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveLog(log: Log): Boolean {
        return try {
            val response = service.saveLog(log)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteLog(id: Int): Boolean {
        return try {
            val response = service.deleteLog(id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateLog(log: Log): Log? {
        return try {
            if (log.id != null) {
                val response = service.updateLog(log.id, log)
                if (response.isSuccessful) response.body() else null
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}