package com.example.recipeapp.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(title: String, subtitle: String? = null) {
    Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
        Text(text = title, style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold))
        if (subtitle != null) Text(text = subtitle, style = TextStyle(fontSize = 24.sp))
    }
}