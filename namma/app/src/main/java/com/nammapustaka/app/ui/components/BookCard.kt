package com.nammapustaka.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammapustaka.app.models.Book
import com.nammapustaka.app.ui.theme.CardBorder
import com.nammapustaka.app.ui.theme.Danger
import com.nammapustaka.app.ui.theme.DangerLight
import com.nammapustaka.app.ui.theme.Gold
import com.nammapustaka.app.ui.theme.Ink
import com.nammapustaka.app.ui.theme.InkMuted
import com.nammapustaka.app.ui.theme.InkSoft
import com.nammapustaka.app.ui.theme.Leaf
import com.nammapustaka.app.ui.theme.LeafLight
import com.nammapustaka.app.ui.theme.categoryColor
import com.nammapustaka.app.ui.theme.categorySoftColor

@Composable
fun BookCard(
    book: Book,
    averageRating: Double,
    reviewCount: Int,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onTap),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
        ) {
            Row(verticalAlignment = Alignment.Top) {
                BookCoverPreview(
                    category = book.category,
                    imagePath = book.imagePath,
                    width = 54.dp,
                    height = 64.dp,
                    iconSize = 28.sp,
                    shape = RoundedCornerShape(14.dp),
                )
                Spacer(modifier = Modifier.weight(1f))
                StatusPill(
                    label = if (book.available) "Available" else "Issued",
                    textColor = if (book.available) Leaf else Danger,
                    backgroundColor = if (book.available) LeafLight else DangerLight,
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = book.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 15.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Ink,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = book.author,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = InkSoft,
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .background(categorySoftColor(book.category), RoundedCornerShape(999.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                ) {
                    Text(
                        text = book.category,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = categoryColor(book.category),
                    )
                }
                if (reviewCount > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Gold,
                        )
                        Text(
                            text = " ${averageRating.format(1)} ($reviewCount)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkMuted,
                        )
                    }
                } else {
                    Text(
                        text = "No reviews yet",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = InkSoft,
                    )
                }
            }
        }
    }
}

private fun Double.format(digits: Int): String = "%.${digits}f".format(this)
