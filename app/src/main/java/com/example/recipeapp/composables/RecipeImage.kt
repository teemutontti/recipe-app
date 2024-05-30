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
import com.example.recipeapp.utils.Utils

/**
 * Composable function that displays a recipe image.
 *
 * @param model The data model for the image. Can be a resource ID, URL, URI or a String.
 * @param painter The [Painter] instance for the image.
 * @param isPreview Indicates whether the image is displayed as a preview.
 * @param onLoadError Callback function called when an error occurs while loading the image.
 * @param onLoadSuccess Callback function called when the image is successfully loaded.
 */
@Composable
fun RecipeImage(
    model: Any? = null,
    painter: Painter? = null,
    isPreview: Boolean = false,
    onLoadError: () -> Unit = {},
    onLoadSuccess: () -> Unit = {},
) {
    var fixedModel = model
    if (model.toString().startsWith("content://")) {
        fixedModel = Uri.parse(model.toString())
    }

    val imageModifier: Modifier = Modifier
        .aspectRatio(Utils.LANDSCAPE_ASPECT_RATIO)
        .clip(RoundedCornerShape(8.dp))
        .fillMaxWidth()

    val previewImageModifier: Modifier = Modifier
        .size(Utils.IMAGE_WIDTH.dp, Utils.IMAGE_HEIGHT.dp)
        .clip(RoundedCornerShape(8.dp))
        .fillMaxWidth()

    if (fixedModel != null) {
        AsyncImage(
            model = fixedModel,
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
