package com.example.recipeapp.utils

object FormattingUtils {
    fun toLowerCaseCapitalizeFirst(str: String): String {
        return str[0] + str.substring(1).lowercase()
    }
}