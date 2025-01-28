package com.example.recipeapp.models

data class Log(
    val id: Int? = null,
    val date: String,
    val time: String,
    val meal: String,
    val userId: Int,
    val foodId: Int,
    var amount: Double,
)