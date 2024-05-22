package com.example.recipeapp.composables

import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipeapp.repositories.RecipeRepository
import com.example.recipeapp.utils.AddableIngredient
import com.example.recipeapp.utils.Utils

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IngredientForm(addIngredient: () -> Unit) {
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)

    var name: String by remember { mutableStateOf(recipeViewModel.ingredientInAddition?.name ?: "") }
    var amount: Int by remember { mutableIntStateOf(recipeViewModel.ingredientInAddition?.amount ?: 1) }
    var unit: String by remember { mutableStateOf(recipeViewModel.ingredientInAddition?.unit ?: "") }

    var nameError by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf(false) }
    var unitError by remember { mutableStateOf(false) }

    fun handleIngredientSave() {
        if (recipeViewModel.ingredientInAddition == null) {
            recipeViewModel.setIngredientInAddition(Utils.emptyAddableIngredient)
        }

        nameError = !Utils.Validator.ingredientName(name)
        amountError = !Utils.Validator.ingredientAmount(amount)
        unitError = !Utils.Validator.ingredientUnit(unit)

        if (!nameError && !amountError && !unitError) {
            recipeViewModel.ingredientInAddition?.let {
                recipeViewModel.setIngredientInAddition(it.copy(
                    name = name,
                    amount = amount,
                    unit = unit,
                ))
            }
            addIngredient()
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
        style = TextStyle(
            color =
            if (amountError) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onBackground
            }
        )
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
        style = TextStyle(color =
        if (unitError) {
            MaterialTheme.colorScheme.error
        }else {
            MaterialTheme.colorScheme.onBackground
        }
        )
    )
    Spacer(modifier = Modifier.height(8.dp))
    // Using FlowRow to wrap larger list to separate lines
    FlowRow(
        horizontalArrangement = Arrangement.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Utils.INGREDIENT_UNITS.forEach {
            TextButton(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(32.dp)
                    .padding(2.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    unit = it
                    unitError = false
                },
                colors =
                if (unitError) {
                    ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onError,
                        containerColor = MaterialTheme.colorScheme.error
                    )
                } else {
                    ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                }
                ,
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