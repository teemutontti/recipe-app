package com.example.recipeapp.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StepIndicator(
    steps: IntRange,
    currentStep: Int,
    handleStepChange: ((Int) -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (handleStepChange != null) TextButton(onClick = { handleStepChange(-1) }) {
            Text("Previous")
        }
        steps.forEach {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .height(16.dp)
                    .width(16.dp)
                    .background(
                        if (currentStep >= it) {
                            MaterialTheme.colorScheme.tertiary
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
                    .border(
                        1.dp,
                        if (currentStep < it) {
                            MaterialTheme.colorScheme.outline
                        } else {
                            Color.Transparent
                        },
                        CircleShape
                    )
            ) {
                if (currentStep > it) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "check",
                        tint = MaterialTheme.colorScheme.scrim,
                        modifier = Modifier.padding(2.dp)
                    )
                } else {
                    Text(
                        text = "${it + 1}",
                        style = TextStyle(
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            fontWeight =
                                if (currentStep >= it) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                }
                            ,
                            color =
                                if (currentStep >= it) {
                                    MaterialTheme.colorScheme.scrim
                                } else {
                                    Color.Gray.copy(alpha = 0.7f)
                                }
                        )
                    )
                }
            }
            if (it < steps.last) Box(
                modifier = Modifier
                    .height(2.dp)
                    .width(56.dp)
                    .background(
                        if (currentStep > it) {
                            MaterialTheme.colorScheme.tertiary
                        } else {
                            MaterialTheme.colorScheme.outline
                        }
                    )
            )
        }
    }
}
