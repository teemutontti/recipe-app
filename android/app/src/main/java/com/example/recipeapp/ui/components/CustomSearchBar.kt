package com.example.recipeapp.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipeapp.viewmodels.SearchViewModel

/**
 * A composable function that displays a custom search bar.
 * It allows users to input a search query and triggers a search action in the provided ViewModel.
 *
 * @param viewModel The SearchViewModel that handles the search logic.
 * @param showResult A boolean flag indicating whether search results are currently being shown.
 * @param handleShowResult A lambda function to handle the visibility of search results.
 */
@Composable
fun CustomSearchBar(
    viewModel: SearchViewModel,
    showResult: Boolean,
    handleShowResult: (Boolean) -> Unit,
) {
    var loading by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    fun handleSearch() {
        Log.d("CustomSearchBar", "Query: $query")
        if (query != "") {
            loading = true
            handleShowResult(true)
            viewModel.search(query)
            loading = false
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .height(36.dp)
    ) {
        TextButton(
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.width(32.dp).height(32.dp),
            onClick = {
                if (showResult) {
                    handleShowResult(false)
                    query = ""
                }
            }
        ) {
            if (showResult) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, "back")
            } else {
                Icon(Icons.Rounded.Search, "search")
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        Box {
            if (query == "") {
                Text(text = "Search any recipe", color = Color.Gray)
            }
            BasicTextField(
                value = query,
                onValueChange = { newValue -> query = newValue },
                singleLine = true,
                keyboardActions = KeyboardActions(onSearch = { handleSearch() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
