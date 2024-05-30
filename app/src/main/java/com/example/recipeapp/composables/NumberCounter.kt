package com.example.recipeapp.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * A composable function that displays a number counter input.
 *
 * @param value The initial value of the counter.
 * @param onNumberChange The callback to be invoked when the number changes.
 * @param prefix The optional text to be displayed before the counter.
 * @param suffix The optional text to be displayed after the counter.
 * @param min The minimum value of the counter.
 * @param max The maximum value of the counter.
 * @param editable Whether the counter is editable or not.
 */
@Composable
fun NumberCounter(
    value: Int = 1,
    onNumberChange: (Int) -> Unit = {},
    prefix: String = "",
    suffix: String = "",
    min: Int = 0,
    max: Int = 100,
    editable: Boolean = false
) {
    var number: Int by remember { mutableIntStateOf(value) }

    fun handleNumberChange(direction: Int? = null, numberStr: String? = null) {
        if (direction != null) {
            val newNumber: Int = number + direction
            if (newNumber in (min + 1)..<max) {
                number += direction
                onNumberChange(number)
            }
        } else if (numberStr != null) {
            val newNumber: Int? = numberStr.toIntOrNull()
            if (newNumber != null && newNumber in (min + 1)..<max) {
                number = newNumber
                onNumberChange(number)
            } else {
                number = 0
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(44.dp)
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
                    .width(44.dp)
                    .height(44.dp),
                onClick = { handleNumberChange(-1) }
            ) {
                Text("-")
            }
            BasicTextField(
                value = "${if (number == 0) "" else number}",
                onValueChange = { handleNumberChange(numberStr = it) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(44.dp),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                readOnly = !editable,
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                ),
            )
            TextButton(
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
                onClick = { handleNumberChange(1) },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .width(44.dp)
                    .height(44.dp)
            ) {
                Text("+")
            }
        }
        if (suffix.isNotEmpty()) Spacer(modifier = Modifier.width(8.dp))
        Text(suffix)
    }
}
