package com.example.recipeapp.services

import com.example.recipeapp.models.Log
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LogService {
    @GET("api/logs")
    suspend fun getLogs(): Response<List<Log>>

    @GET("api/logs/by-date")
    suspend fun getLogsByDateAndUser(
        @Query("date") date: String,
        @Query("userId") userId: Int,
        @Header("Authorization") authHeader: String,
    ): Response<List<Log>>

    @POST("api/logs")
    suspend fun saveLog(
        @Body log: Log,
        @Header("Authorization") authHeader: String,
    ): Response<Log>

    @PATCH("api/logs/{id}")
    suspend fun updateLog(
        @Path("id") id: Int,
        @Body log: Log,
        @Header("Authorization") authHeader: String,
    ): Response<Log>

    @DELETE("api/logs/{id}")
    suspend fun deleteLog(
        @Path("id") id: Int,
        @Header("Authorization") authHeader: String,
    ): Response<Void>
}