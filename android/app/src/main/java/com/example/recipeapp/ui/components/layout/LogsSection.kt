package com.example.recipeapp.ui.components.layout

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.example.recipeapp.models.database.Log
import com.example.recipeapp.ui.components.misc.ItemDivider
import com.example.recipeapp.viewmodels.ViewModelWrapper

@Composable
fun LogsSection(
    title: String,
    logs: List<Log>,
    viewModels: ViewModelWrapper,
) {
    TitledContainer(title) {
        if (logs.isEmpty()) {
            Text(
                text = "No foods logged.",
                style = TextStyle(color = MaterialTheme.colorScheme.outline)
            )
        } else {
            logs.forEachIndexed { index, log ->
                LogRow(log = log, viewModel = viewModels.logsScreen)
                if (index < logs.size - 1) ItemDivider()
            }
        }
    }
}