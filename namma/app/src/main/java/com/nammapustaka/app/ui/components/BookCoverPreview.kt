package com.nammapustaka.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.nammapustaka.app.ui.theme.categoryEmoji
import com.nammapustaka.app.ui.theme.categorySoftColor
import java.io.File

@Composable
fun BookCoverPreview(
    category: String,
    imagePath: String?,
    width: Dp = 92.dp,
    height: Dp = 118.dp,
    iconSize: androidx.compose.ui.unit.TextUnit = androidx.compose.ui.unit.TextUnit.Unspecified,
    shape: Shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
) {
    val hasImage = !imagePath.isNullOrBlank() && File(imagePath).exists()

    Box(
        modifier = Modifier
            .size(width = width, height = height)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(categorySoftColor(category), androidx.compose.ui.graphics.Color.White),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (hasImage) {
            AsyncImage(
                model = File(imagePath),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            Text(
                text = categoryEmoji(category),
                fontSize = if (iconSize == androidx.compose.ui.unit.TextUnit.Unspecified) 44.sp else iconSize,
                fontWeight = FontWeight.Black,
            )
        }
    }
}
