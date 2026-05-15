package com.nammapustaka.app.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammapustaka.app.models.Book
import com.nammapustaka.app.models.BorrowRecord
import com.nammapustaka.app.ui.components.BookCard
import com.nammapustaka.app.ui.components.BookCoverPreview
import com.nammapustaka.app.ui.components.SectionCard
import com.nammapustaka.app.ui.components.StatusPill
import com.nammapustaka.app.ui.theme.Gold
import com.nammapustaka.app.ui.theme.GoldLight
import com.nammapustaka.app.ui.theme.Ink
import com.nammapustaka.app.ui.theme.InkMuted
import com.nammapustaka.app.ui.theme.InkSoft
import com.nammapustaka.app.ui.theme.Leaf
import com.nammapustaka.app.ui.theme.LeafLight
import com.nammapustaka.app.ui.theme.PageBackground
import com.nammapustaka.app.ui.theme.Saffron
import com.nammapustaka.app.ui.theme.Sky
import com.nammapustaka.app.ui.theme.SkyLight
import com.nammapustaka.app.ui.theme.categoryEmoji
import com.nammapustaka.app.ui.theme.categorySoftColor
import com.nammapustaka.app.utils.Formatters
import com.nammapustaka.app.viewmodel.AppUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LibraryScreen(
    uiState: AppUiState,
    onLogout: () -> Unit,
    onRefresh: suspend () -> Unit = {},
    onSearchChanged: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onOpenBook: (Long) -> Unit,
) {
    val goalProgress by animateFloatAsState(uiState.goalProgress, label = "goalProgress")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Saffron,
                                RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
                            )
                            .padding(start = 18.dp, end = 18.dp, top = 18.dp, bottom = 22.dp),
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.Top) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Namma Pustaka",
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Black,
                                    )
                                    Text(
                                        text = "ನಮ್ಮ ಪುಸ್ತಕ · ಗ್ರಾಮೀಣ ಶಾಲೆಗಳ ಸ್ಮಾರ್ಟ್ ಗ್ರಂಥಾಲಯ",
                                        color = Color(0xFFFDE7D8),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(top = 4.dp),
                                    )
                                }
                                IconButton(
                                    onClick = onLogout,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color(0x33FFFFFF)),
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Logout,
                                        contentDescription = "Logout",
                                        tint = Color.White,
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.padding(top = 18.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x33FFFFFF)),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = uiState.currentUser?.initials ?: "NP",
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                    )
                                }
                                Column(modifier = Modifier.padding(start = 12.dp)) {
                                    Text(
                                        text = uiState.currentUser?.name ?: "Reader",
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Black,
                                    )
                                    Text(
                                        text = if (uiState.isStudent) {
                                            "ವಿದ್ಯಾರ್ಥಿ · Roll No. ${uiState.currentUser?.roll ?: "--"}"
                                        } else {
                                            "Teacher dashboard access"
                                        },
                                        color = Color(0xFFFDE7D8),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(top = 3.dp),
                                    )
                                    if (uiState.isStudent && uiState.studentRank > 0) {
                                        Box(
                                            modifier = Modifier
                                                .padding(top = 8.dp)
                                                .background(Gold, RoundedCornerShape(999.dp))
                                                .padding(horizontal = 12.dp, vertical = 5.dp),
                                        ) {
                                            Text(
                                                text = "Rank #${uiState.studentRank} this month",
                                                color = Color(0xFF7A5700),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Black,
                                            )
                                        }
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                HeaderStat(
                                    label = if (uiState.isStudent) "Books Read" else "Books",
                                    value = if (uiState.isStudent) uiState.booksRead.toString() else uiState.teacherStats.totalBooks.toString(),
                                    modifier = Modifier.weight(1f),
                                )
                                HeaderStat(
                                    label = if (uiState.isStudent) "Pages" else "Available",
                                    value = if (uiState.isStudent) uiState.pagesRead.toString() else uiState.teacherStats.availableBooks.toString(),
                                    modifier = Modifier.weight(1f),
                                )
                                HeaderStat(
                                    label = if (uiState.isStudent) "Active" else "Overdue",
                                    value = if (uiState.isStudent) uiState.activeBorrowCount.toString() else uiState.teacherStats.overdueBooks.toString(),
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        if (uiState.isStudent) {
                            SectionCard {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Monthly reading goal",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "${uiState.pagesRead} / 200 pages",
                                        fontWeight = FontWeight.Bold,
                                        color = InkMuted,
                                    )
                                }
                                LinearProgressIndicator(
                                    progress = { goalProgress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp),
                                    color = Saffron,
                                    trackColor = Color(0xFFEEE8DF),
                                )
                                Text(
                                    text = if (uiState.pagesRead >= 200) {
                                        "Goal reached. ಶಾಭಾಶ್!"
                                    } else {
                                        "${200 - uiState.pagesRead} more pages to hit your goal."
                                    },
                                    color = InkSoft,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(top = 8.dp),
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        TextField(
                            value = uiState.searchQuery,
                            onValueChange = onSearchChanged,
                            label = { Text("Search title or author...") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                        )

                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 14.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            uiState.categories.forEach { category ->
                                FilterChip(
                                    selected = uiState.selectedCategory == category,
                                    onClick = { onCategorySelected(category) },
                                    label = { Text(category) },
                                )
                            }
                        }
                    }
                }
            }

            if (uiState.isStudent) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SectionTitle("Currently Borrowed", modifier = Modifier.padding(horizontal = 16.dp))
                }
                if (uiState.myActiveBorrows.isEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        SectionCard(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Text(
                                text = "No active borrows. QR scan ಮಾಡಿ ಪುಸ್ತಕ ತೆಗೆದುಕೊಳ್ಳಿ.",
                                color = InkSoft,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                } else {
                    items(uiState.myActiveBorrows, span = { GridItemSpan(maxLineSpan) }) { borrow ->
                        BorrowedBookTile(
                            borrow = borrow,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            onTap = { onOpenBook(borrow.book.id) },
                        )
                    }
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        SectionTitle("Milestones")
                        FlowRow(
                            modifier = Modifier.padding(top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            MilestoneChip("📘", "First Borrow", uiState.myBorrowRecords.isNotEmpty())
                            MilestoneChip("🔥", "2 Books Read", uiState.booksRead >= 2)
                            MilestoneChip("⭐", "First Review", uiState.hasAnyUserReview)
                            MilestoneChip("🏆", "200 Pages", uiState.pagesRead >= 200)
                        }
                    }
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        SectionTitle("Recent Activity")
                        SectionCard(modifier = Modifier.padding(top = 10.dp)) {
                            if (uiState.myBorrowRecords.isEmpty()) {
                                Text(
                                    text = "Your reading activity will appear here after the first borrow.",
                                    color = InkSoft,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    uiState.myBorrowRecords.take(3).forEach { borrow ->
                                        Row {
                                            Box(
                                                modifier = Modifier
                                                    .size(34.dp)
                                                    .clip(CircleShape)
                                                    .background(categorySoftColor(borrow.book.category)),
                                                contentAlignment = Alignment.Center,
                                            ) {
                                                Text(categoryEmoji(borrow.book.category))
                                            }
                                            Column(modifier = Modifier.padding(start = 10.dp)) {
                                                Text(
                                                    text = if (borrow.isReturned) {
                                                        "Returned ${borrow.book.title}"
                                                    } else {
                                                        "Borrowed ${borrow.book.title}"
                                                    },
                                                    fontWeight = FontWeight.Bold,
                                                )
                                                Text(
                                                    text = Formatters.shortDate(borrow.transaction.issueDateMillis),
                                                    color = InkSoft,
                                                    fontSize = 12.sp,
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        SectionTitle("Library Snapshot")
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            SnapshotMetric(
                                label = "Borrowed now",
                                value = uiState.teacherStats.borrowedBooks.toString(),
                                tint = Sky,
                                background = SkyLight,
                                modifier = Modifier.weight(1f),
                            )
                            SnapshotMetric(
                                label = "Overdue books",
                                value = uiState.teacherStats.overdueBooks.toString(),
                                tint = Gold,
                                background = GoldLight,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                SectionTitle("Book Catalog", modifier = Modifier.padding(horizontal = 16.dp))
            }

            if (uiState.filteredBooks.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SectionCard(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "No books match this filter.",
                            color = InkSoft,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            } else {
                items(uiState.filteredBooks) { book ->
                    BookCard(
                        book = book,
                        averageRating = uiState.averageRatingForBook(book.id),
                        reviewCount = uiState.reviewCountForBook(book.id),
                        onTap = { onOpenBook(book.id) },
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(248.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderStat(label: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0x33FFFFFF))
            .padding(12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
            )
            Text(
                text = label,
                color = Color(0xFFFDE7D8),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        modifier = modifier,
        fontSize = 18.sp,
        fontWeight = FontWeight.Black,
        color = Ink,
    )
}

@Composable
private fun MilestoneChip(icon: String, label: String, earned: Boolean) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (earned) GoldLight else PageBackground)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(icon)
        Text(
            text = label,
            modifier = Modifier.padding(start = 8.dp),
            color = if (earned) Color(0xFF7A5700) else InkSoft,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun SnapshotMetric(
    label: String,
    value: String,
    tint: Color,
    background: Color,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = value,
                color = tint,
                fontWeight = FontWeight.Black,
                fontSize = 22.sp,
            )
            Text(
                text = label,
                modifier = Modifier.padding(top = 4.dp),
                color = InkSoft,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(background)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
            ) {
                Text(
                    text = if (label.contains("Overdue")) "Attention" else "Live data",
                    color = tint,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun BorrowedBookTile(
    borrow: BorrowRecord,
    modifier: Modifier = Modifier,
    onTap: () -> Unit,
) {
    val isOverdue = borrow.isOverdue
    val textColor = when {
        isOverdue -> com.nammapustaka.app.ui.theme.Danger
        borrow.daysLeft <= 2 -> Gold
        else -> Leaf
    }
    val backgroundColor = when {
        isOverdue -> com.nammapustaka.app.ui.theme.DangerLight
        borrow.daysLeft <= 2 -> GoldLight
        else -> LeafLight
    }

    SectionCard(
        modifier = modifier.clickable(onClick = onTap),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BookCoverPreview(
                category = borrow.book.category,
                imagePath = borrow.book.imagePath,
                width = 54.dp,
                height = 64.dp,
                iconSize = 28.sp,
                shape = RoundedCornerShape(14.dp),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
            ) {
                Text(
                    text = borrow.book.title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                )
                Text(
                    text = borrow.book.author,
                    modifier = Modifier.padding(top = 4.dp),
                    color = InkSoft,
                    fontWeight = FontWeight.SemiBold,
                )
                StatusPill(
                    label = Formatters.dueLabel(borrow),
                    textColor = textColor,
                    backgroundColor = backgroundColor,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}
