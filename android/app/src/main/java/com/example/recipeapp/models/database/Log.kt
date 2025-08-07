package com.example.recipeapp.models.database

import java.util.UUID

data class Log(
    val id: UUID? = null,
    val date: String,
    val time: String,
    val meal: String,
    val userId: UUID,
    val foodId: UUID,
    var amount: Double,
)