package com.example.recipeapp.utils

enum class AlertType {
    ERROR,
    WARNING,
    INFO,
}

data class Alert(
    val message: String,
    val type: AlertType? = AlertType.ERROR,
)