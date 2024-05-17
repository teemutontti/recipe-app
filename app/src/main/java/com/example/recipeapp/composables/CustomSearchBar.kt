package com.example.recipeapp.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipeapp.repositories.RecipeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CustomSearchBar(
    navController: NavController,
    recipeViewModel: RecipeRepository,
    incomingQuery: String? = null,
    inSearchResultsScreen: Boolean = false
) {
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    fun handleSearch() {
        if (query != "") {
            loading = true
            CoroutineScope(Dispatchers.Default).launch {
                recipeViewModel.searchRecipes(context, query)
                loading = false
            }
            if (!inSearchResultsScreen) {
                navController.navigate("search_results/${query}")
            }
        }
    }

    LaunchedEffect(Unit) {
        if (incomingQuery != null) {
            query = incomingQuery
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .height(36.dp)
    ) {
        Icon(Icons.Rounded.Search, "Search icon", tint = MaterialTheme.colorScheme.primary)
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
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                textStyle = TextStyle(fontSize = 16.sp),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
    /* OLD CODE
    TextField(
        value = query,
        onValueChange = { query = it },
        // TODO: Add search format instructions
        //supportingText = { Text("Test") },
        placeholder = { Text("Search...") },
        leadingIcon = { Icon(Icons.Rounded.Search, "search icon") },
        singleLine = true,
        keyboardActions = KeyboardActions(onSearch = { handleSearch() }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(0.dp),
        shape = RoundedCornerShape(24.dp),
        textStyle = TextStyle(fontSize = 16.sp)
    )

     */
}