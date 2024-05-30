package com.example.recipeapp.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.recipeapp.R
import com.example.recipeapp.viewmodels.ViewModelWrapper

data class Category(
    val drawableId: Int,
    val title: String,
    val query: String,
)

@Composable
fun CategoryShelf(
    viewModels: ViewModelWrapper,
    showResult: Boolean,
    handleShowResult: (Boolean) -> Unit
){
    val categories = listOf(
        Category(R.drawable.chicken, "Chicken", "chicken"),
        Category(R.drawable.beef, "Beef", "beef"),
        Category(R.drawable.pork, "Pork", "pork"),
        Category(R.drawable.fish, "Seafood", "seafood"),
        Category(R.drawable.spaghetti, "Pasta", "pasta"),
        Category(R.drawable.rice, "Rice", "rice"),
        Category(R.drawable.vegetable, "Vegetable", "vegetable"),
        Category(R.drawable.fruit, "Fruit", "fruit"),
    )

    fun handleCategoryClick(categoryQuery: String) {
        viewModels.search.search(categoryQuery)
        handleShowResult(true)
    }

    LazyRow {
        itemsIndexed(categories) { index, it ->
            TextButton(
                modifier = Modifier.width(96.dp).height(96.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = { handleCategoryClick(it.query) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        contentScale = ContentScale.Fit,
                        painter = painterResource(id = it.drawableId),
                        contentDescription = "category",
                        modifier = Modifier.weight(0.8f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it.title)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}
