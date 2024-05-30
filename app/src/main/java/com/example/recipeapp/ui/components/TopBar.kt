package com.example.recipeapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipeapp.utils.LocalApplicationContext
import com.example.recipeapp.utils.Utils.checkInternetConnection

/**
 * Composable function that displays a top bar with a title and optional subtitle.
 *
 * @param title The title to display.
 * @param subtitle The optional subtitle to display.
 */
@Composable
fun TopBar(title: String, subtitle: String? = null) {
    val context = LocalApplicationContext.current
    val networkConnected = checkInternetConnection(context)

    Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
        Text(title, style = MaterialTheme.typography.headlineLarge)
        if (subtitle != null) Text(subtitle)
        if (!networkConnected) {
            Spacer(modifier = Modifier.height(8.dp))
            UserFeedbackMessage("No internet connection", type = "warning")
        }
    }
}
