package com.example.recipeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.recipeapp.models.ShoppingListItem
import com.example.recipeapp.viewmodels.ShoppingListViewModel

@Composable
fun ShoppingListItemForm(viewModel: ShoppingListViewModel) {
    var show by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }
    var newItemNote by remember { mutableStateOf("") }

    fun handleOnSubmit() {
        if (newItemName.isNotEmpty()) {
            val newShoppingListItem = ShoppingListItem(newItemName, newItemNote)
            viewModel.add(newShoppingListItem)
            newItemName = ""
            newItemNote = ""
        }
    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(start = 16.dp, end = 16.dp, bottom = if (show) 16.dp else 0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add new item", style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = { show = !show }) {
                Text(if (show) "Hide" else "Show")
                Icon(
                    imageVector =
                        if (show) Icons.Rounded.ArrowDropUp
                        else Icons.Rounded.ArrowDropDown,
                    contentDescription = "arrow down"
                )
            }
        }
        if (show) {
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = newItemName,
                label = { Text("Name") },
                onValueChange = { newItemName = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = newItemNote,
                label = { Text("Note") },
                onValueChange = { newItemNote = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { handleOnSubmit() }
            ) {
                Text("Save")
            }
        }
    }
}