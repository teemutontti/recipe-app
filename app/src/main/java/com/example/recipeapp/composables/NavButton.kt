package com.example.recipeapp.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipeapp.ui.theme.backgroundDark

@Composable
fun NavButton(
    selected: Boolean,
    onClick: () -> Unit,
    iconVector: ImageVector,
    text: String? = null
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor =
                if (selected) MaterialTheme.colorScheme.inverseSurface
                else MaterialTheme.colorScheme.background,
            contentColor =
                if (selected) MaterialTheme.colorScheme.onSecondary
                else MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier.width(112.dp),
        onClick = onClick
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = iconVector, contentDescription = "$text icon")
            if (text != null) Text(
                text = text,
                style = TextStyle(fontSize = 12.sp),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}