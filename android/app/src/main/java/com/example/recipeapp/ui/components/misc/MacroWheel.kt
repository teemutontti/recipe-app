package com.example.recipeapp.ui.components.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * A composable function that circular progression bar.
 */
@Composable
fun MacroWheel(max: Number, title: String, color: Color, size: String = "large") {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {
            Box(modifier = Modifier
                .width(if (size === "large") 175.dp else 92.dp)
                .height(if (size === "large") 175.dp else 92.dp)
                .clip(CircleShape)
                .background(color)
            )
            Box(modifier = Modifier
                .width(if (size === "large") 155.dp else 84.dp)
                .height(if (size === "large") 155.dp else 84.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (size === "large") Text(text = title)
                Text(
                    text = "1200",
                    style =
                        if(size === "large") MaterialTheme.typography.headlineLarge
                        else MaterialTheme.typography.headlineMedium)
                Text(text = "/${max}")
            }
        }
        if (size === "small") Text(text = title)

    }
}