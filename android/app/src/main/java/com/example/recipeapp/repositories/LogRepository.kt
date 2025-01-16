package com.example.recipeapp.repositories

import com.example.recipeapp.models.Log
import com.example.recipeapp.services.RetrofitInstance
import com.example.recipeapp.utils.Result
import java.time.LocalDate

class LogRepository {
    private val retrofitInstance = RetrofitInstance()
    private val service = retrofitInstance.logsService

    suspend fun getLogsByDate(date: LocalDate): Result<List<Log>> {
        return try {
            val response = service.getLogsByDate(date.toString())
            val body = response.body()
            android.util.Log.d("LogRepository", body.toString())

            val body2 = response.body()
            android.util.Log.d("LogRepository", body2.toString())

            if (response.isSuccessful && response.body() != null) Result.success(response.body())
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun saveLog(log: Log): Result<Boolean> {
        return try {
            val response = service.saveLog(log)
            if (response.isSuccessful) Result.success(true)
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun deleteLog(id: Int): Result<Boolean> {
        return try {
            val response = service.deleteLog(id)
            if (response.isSuccessful) Result.success(true)
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun updateLog(log: Log): Result<Log> {
        return try {
            if (log.id != null) {
                val response = service.updateLog(log.id, log)
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