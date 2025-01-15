package com.example.recipeapp.repositories

import com.example.recipeapp.models.Log
import com.example.recipeapp.services.RetrofitInstance
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate

interface LogService {
    @GET("api/logs")
    suspend fun getLogs(): Response<List<Log>>

    @GET("api/logs/by-date")
    suspend fun getLogsByDate(@Query("date") date: String): Response<List<Log>>

    @POST("api/logs")
    suspend fun saveLog(@Body log: Log): Response<Log>

    @PATCH("api/logs/{id}")
    suspend fun updateLog(@Path("id") id: Int, @Body log: Log): Response<Log>

    @DELETE("api/logs/{id}")
    suspend fun deleteLog(@Path("id") id: Int): Response<Void>
}

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