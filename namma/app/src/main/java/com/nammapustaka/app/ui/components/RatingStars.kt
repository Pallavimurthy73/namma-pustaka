package com.nammapustaka.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nammapustaka.app.ui.theme.Gold

@Composable
fun RatingStars(
    rating: Int,
    modifier: Modifier = Modifier,
    size: Dp = 22.dp,
    onChanged: ((Int) -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        repeat(5) { index ->
            val starValue = index + 1
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (starValue <= rating) Gold else Color(0xFFDAD5CE),
                modifier = Modifier
                    .then(
                        if (onChanged != null) {
                            Modifier.clickable { onChanged(starValue) }
                        } else {
                            Modifier
                        },
                    )
                    .size(size)
                    .padding(1.dp)
            )
        }
    }
}
