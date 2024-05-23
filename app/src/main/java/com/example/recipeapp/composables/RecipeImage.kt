package com.example.recipeapp.composables

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.recipeapp.utils.Utils

@Composable
fun RecipeImage(
    model: Any? = null,
    painter: Painter? = null,
    isPreview: Boolean = false,
    onLoadError: () -> Unit = {},
    onLoadSuccess: () -> Unit = {},
) {
    var _model = model
    if (model.toString().startsWith("content://")) {
        _model = Uri.parse(model.toString())
    }

    val imageModifier: Modifier = Modifier
        .aspectRatio(Utils.LANDSCAPE_ASPECT_RATIO)
        .clip(RoundedCornerShape(8.dp))
        .fillMaxWidth()

    val previewImageModifier: Modifier = Modifier
        .size(Utils.IMAGE_WIDTH.dp, Utils.IMAGE_HEIGHT.dp)
        .clip(RoundedCornerShape(8.dp))
        .fillMaxWidth()

    if (model != null) {
        AsyncImage(
            model = _model,
            contentDescription = "recipe",
            contentScale = ContentScale.Crop,
            modifier = if (isPreview) previewImageModifier else imageModifier,
            onError = { onLoadError() },
            onSuccess = { onLoadSuccess() },
        )
    } else if (painter != null) {
        Image(
            painter = painter,
            contentDescription = "recipe",
            contentScale = ContentScale.Crop,
            modifier = if (isPreview) previewImageModifier else imageModifier
        )
    }


}