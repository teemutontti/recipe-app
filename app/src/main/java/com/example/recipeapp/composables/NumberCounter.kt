package com.example.recipeapp.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun NumberCounter(
    defaultValue: Int,
    onNumberChange: (Int) -> Unit = {},
    prefix: String = "",
    suffix: String = "",
    min: Int = 0,
    max: Int = 100,
) {
    var number: Int by remember { mutableIntStateOf(defaultValue) }

    fun handleNumberChange(direction: Int) {
        val newNumber = number + direction
        if (newNumber in (min + 1)..<max) {
            number += direction
            onNumberChange(number)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(32.dp)

    ) {
        Text(prefix)
        if (prefix.isNotEmpty()) Spacer(modifier = Modifier.width(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            TextButton(
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .width(32.dp)
                    .height(32.dp),
                onClick = { handleNumberChange(-1) }
            ) {
                Text("-")
            }
            Text(
                text = "$number",
                style = TextStyle(textAlign = TextAlign.Center),
                modifier = Modifier.width(32.dp)
            )
            TextButton(
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .width(32.dp)
                    .height(32.dp),
                onClick = { handleNumberChange(1) }
            ) {
                Text("+")
            }
        }
        if (suffix.isNotEmpty()) Spacer(modifier = Modifier.width(8.dp))
        Text(suffix)
    }
}