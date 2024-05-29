package com.example.recipeapp.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipeapp.models.room.PersonalIngredient
import com.example.recipeapp.utils.Utils

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IngredientForm(addIngredient: (PersonalIngredient) -> Unit) {
    var name: String by remember { mutableStateOf("") }
    var amount: Int by remember { mutableIntStateOf(1) }
    var unit: String by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf(false) }
    var unitError by remember { mutableStateOf(false) }

    fun handleIngredientSave() {
        nameError = !Utils.Validator.ingredientName(name)
        amountError = !Utils.Validator.ingredientAmount(amount)
        unitError = !Utils.Validator.ingredientUnit(unit)

        if (!nameError && !amountError && !unitError) {
            addIngredient(
                PersonalIngredient(
                    name = name,
                    unit = unit,
                    amount = amount.toFloat(),
                )
            )
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Ingredients",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp)
        )
        TextButton(
            contentPadding = PaddingValues(horizontal = 8.dp),
            modifier = Modifier.fillMaxHeight(),
            onClick = { handleIngredientSave() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.Save, "save")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Save")
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
    TextField(
        value = name,
        label = { Text("Name") },
        onValueChange = { name = it },
        isError = nameError,
        supportingText = { if (nameError) Text("Enter proper name") }
    )
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "Select amount:",
        color = if (amountError) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.onBackground
        }
    )
    Spacer(modifier = Modifier.height(8.dp))
    NumberCounter(
        value = amount,
        max = 1000,
        onNumberChange = { amount = it },
        editable = true
    )
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "Select a unit:",
        color = if (unitError) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.onBackground
        }
    )
    Spacer(modifier = Modifier.height(8.dp))

    // Using FlowRow to wrap row to separate lines on overflow
    FlowRow(
        horizontalArrangement = Arrangement.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Utils.INGREDIENT_UNITS.forEach {
            TextButton(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(32.dp).padding(2.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    unit = it
                    unitError = false
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = if (unitError) {
                        MaterialTheme.colorScheme.onError
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    },
                    containerColor = if (unitError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                ),
                border = BorderStroke(
                    width = 2.dp,
                    color =
                    if (it == unit) {
                        MaterialTheme.colorScheme.outline
                    } else {
                        Color.Transparent
                    }
                ),
            ) {
                Text(it)
            }
        }
    }
}