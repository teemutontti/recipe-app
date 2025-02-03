package com.example.recipeapp.ui.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.recipeapp.models.Food
import com.example.recipeapp.ui.components.layout.TitledContainer
import com.example.recipeapp.utils.FormattingUtils
import com.example.recipeapp.viewmodels.LogsScreenViewModel

@Composable
fun FoodForm(viewModel: LogsScreenViewModel) {
    var name by remember { mutableStateOf("") }
    var barcode by remember { mutableStateOf("") }
    var servingSize by remember { mutableStateOf("100") }
    var calories by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }

    LaunchedEffect(name, barcode, servingSize, calories, carbs, protein, fat) {
        viewModel.setSavableFood(null)
        if (name.isNotEmpty() && servingSize.isNotEmpty() && calories.isNotEmpty()) {
            val food = Food(
                name = name,
                barcode = barcode,
                servingSize = servingSize.toInt(),
                calories = calories.toDouble(),
                carbs = if (carbs.isNotEmpty()) {
                    FormattingUtils.stringToDouble(carbs)
                } else 0.0,
                protein = if (protein.isNotEmpty()) {
                    FormattingUtils.stringToDouble(protein)
                } else 0.0,
                fat = if (fat.isNotEmpty()) {
                    FormattingUtils.stringToDouble(fat)
                } else 0.0,
                createdBy = viewModel.getUserId(),
                editedBy = viewModel.getUserId(),
            )
            viewModel.setSavableFood(food)
        }
    }

    Column {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name *") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
        )
        Spacer(modifier = Modifier.padding(vertical = 24.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            NutrientInputRow("Nutrients per", servingSize,
                suffixText = "g",
                fillMaxWidth = false,
                onChange = { servingSize = it }
            )
        }
        TitledContainer(
            title = "Nutrients",
            titleSize = MaterialTheme.typography.titleLarge,
            backgroundColor = MaterialTheme.colorScheme.background,
        ) {
            Column(modifier = Modifier.padding(start = 8.dp)) {
                NutrientInputRow("Calories *", calories, 120.dp, "kcal") {
                    calories = it
                }
                NutrientInputRow("Carbohydrates", carbs, suffixText = "g") {
                    carbs = it
                }
                NutrientInputRow("Protein", protein, suffixText = "g") {
                    protein = it
                }
                NutrientInputRow("Fat", fat, suffixText = "g") {
                    fat = it
                }
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        Row {
            TextField(
                value = barcode,
                onValueChange = { barcode = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = { Text("Barcode") },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.clip(RoundedCornerShape(6.dp, 0.dp, 0.dp, 6.dp))
            )
            Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            IconButton(
                onClick = { /*TODO: Add barcode scanner implementation*/ },
                modifier = Modifier
                    .clip(RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .height(56.dp)
                    .width(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "Barcode Scanner",
                )
            }
        }
    }
}