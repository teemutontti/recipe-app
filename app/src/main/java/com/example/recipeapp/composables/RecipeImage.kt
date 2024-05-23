package com.example.recipeapp.composables

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun RecipeImage(model: Any? = null, painter: Painter? = null) {
    val imageModifier: Modifier = Modifier
        .aspectRatio(1.5f)
        .clip(RoundedCornerShape(8.dp))
        .size(width = 288.dp, height = 162.dp)
        .fillMaxWidth()

    if (model != null) {
        AsyncImage(
            model = model,
            contentDescription = "recipe",
            contentScale = ContentScale.Crop,
            modifier = imageModifier
        )
    } else if (painter != null) {
        Image(
            painter = painter,
            contentDescription = "recipe",
            contentScale = ContentScale.Crop,
            modifier = imageModifier
        )
    }
}