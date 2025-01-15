package com.example.recipeapp.services

import com.example.recipeapp.models.Log
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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