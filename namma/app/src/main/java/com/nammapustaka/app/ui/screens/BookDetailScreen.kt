package com.nammapustaka.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammapustaka.app.ui.components.BookCoverPreview
import com.nammapustaka.app.ui.components.RatingStars
import com.nammapustaka.app.ui.components.SectionCard
import com.nammapustaka.app.ui.components.StatusPill
import com.nammapustaka.app.ui.theme.Danger
import com.nammapustaka.app.ui.theme.DangerLight
import com.nammapustaka.app.ui.theme.Gold
import com.nammapustaka.app.ui.theme.Ink
import com.nammapustaka.app.ui.theme.InkMuted
import com.nammapustaka.app.ui.theme.InkSoft
import com.nammapustaka.app.ui.theme.Leaf
import com.nammapustaka.app.ui.theme.LeafLight
import com.nammapustaka.app.ui.theme.Sky
import com.nammapustaka.app.ui.theme.SkyLight
import com.nammapustaka.app.ui.theme.categoryColor
import com.nammapustaka.app.ui.theme.categorySoftColor
import com.nammapustaka.app.viewmodel.AppUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    uiState: AppUiState,
    bookId: Long,
    onBack: () -> Unit,
    onSaveReview: suspend (Long, Int, String) -> String?,
    onNavigateToScanner: () -> Unit,
) {
    val book = uiState.books.firstOrNull { it.id == bookId }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (book == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Book Details") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                        }
                    },
                )
            },
        ) { paddingValues ->
            SectionCard(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
                Text("The selected book could not be found.")
            }
        }
        return
    }

    val reviews = uiState.reviewsForBook(bookId)
    val currentReview = reviews.firstOrNull { it.userId == uiState.currentUser?.id }
    var selectedRating by rememberSaveable(bookId) { mutableIntStateOf(0) }
    var reviewText by rememberSaveable(bookId) { mutableStateOf("") }

    LaunchedEffect(currentReview?.id) {
        selectedRating = currentReview?.rating ?: 0
        reviewText = currentReview?.review.orEmpty()
    }

    val isCurrentStudentBorrower = uiState.myActiveBorrows.any { it.book.id == bookId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SectionCard {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    BookCoverPreview(
                        category = book.category,
                        imagePath = book.imagePath,
                        width = 92.dp,
                        height = 118.dp,
                        iconSize = 46.sp,
                    )
                    Text(
                        text = book.title,
                        modifier = Modifier.padding(top = 16.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Ink,
                    )
                    Text(
                        text = book.author,
                        modifier = Modifier.padding(top = 6.dp),
                        color = InkSoft,
                        fontWeight = FontWeight.Bold,
                    )
                    Row(
                        modifier = Modifier.padding(top = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        StatusPill(book.category, categoryColor(book.category), categorySoftColor(book.category))
                        StatusPill(
                            if (book.available) "Available" else "Issued",
                            if (book.available) Leaf else Danger,
                            if (book.available) LeafLight else DangerLight,
                        )
                        StatusPill("${book.pages} pages", Sky, SkyLight)
                    }
                }
            }

            SectionCard {
                Text(
                    text = "Kannada Summary",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                )
                Text(
                    text = book.summary,
                    modifier = Modifier.padding(top = 10.dp),
                    color = InkMuted,
                    lineHeight = 24.sp,
                )
            }

            SectionCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Student Reviews",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (reviews.isNotEmpty()) {
                        Icon(Icons.Rounded.Star, contentDescription = null, tint = Gold)
                        Text(
                            text = uiState.averageRatingForBook(bookId).toString().take(3),
                            fontWeight = FontWeight.ExtraBold,
                        )
                    }
                }

                if (uiState.isStudent) {
                    Text(
                        text = "Your rating",
                        modifier = Modifier.padding(top = 14.dp),
                        fontWeight = FontWeight.Bold,
                        color = InkMuted,
                    )
                    RatingStars(
                        rating = selectedRating,
                        onChanged = { selectedRating = it },
                        modifier = Modifier.padding(top = 8.dp),
                    )
                    TextField(
                        value = reviewText,
                        onValueChange = {
                            if (it.length <= 120) reviewText = it
                        },
                        label = { Text("Short review") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        minLines = 3,
                    )
                    ElevatedButton(
                        onClick = {
                            scope.launch {
                                val message = onSaveReview(bookId, selectedRating, reviewText)
                                snackbarHostState.showSnackbar(message ?: "Review saved successfully.")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    ) {
                        Text("Save Review")
                    }
                }

                if (reviews.isEmpty()) {
                    Text(
                        text = "No reviews yet. Be the first reader to share one.",
                        modifier = Modifier.padding(top = 16.dp),
                        color = InkSoft,
                        fontWeight = FontWeight.SemiBold,
                    )
                } else {
                    Column(
                        modifier = Modifier.padding(top = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        reviews.forEach { review ->
                            SectionCard(
                                padding = androidx.compose.foundation.layout.PaddingValues(14.dp),
                            ) {
                                RatingStars(rating = review.rating)
                                Text(
                                    text = review.review,
                                    modifier = Modifier.padding(top = 8.dp),
                                    color = InkMuted,
                                    lineHeight = 22.sp,
                                )
                            }
                        }
                    }
                }
            }

            ElevatedButton(
                onClick = onNavigateToScanner,
                enabled = book.available && !isCurrentStudentBorrower,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.Rounded.QrCodeScanner, contentDescription = null)
                Text(
                    text = if (isCurrentStudentBorrower) "Already borrowed by you" else "Scan QR to Borrow",
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
        }
    }
}
