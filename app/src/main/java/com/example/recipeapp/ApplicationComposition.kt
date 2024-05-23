package com.example.recipeapp

import android.content.Context
import androidx.compose.runtime.compositionLocalOf

val ApplicationContext = compositionLocalOf<Context> { error("No Shared Prefs found") }