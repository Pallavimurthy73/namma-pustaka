package com.nammapustaka.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AssignmentTurnedIn
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.LibraryBooks
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammapustaka.app.models.Book
import com.nammapustaka.app.models.BorrowRecord
import com.nammapustaka.app.repository.AuthRepository
import com.nammapustaka.app.ui.components.BookCoverPreview
import com.nammapustaka.app.ui.components.MetricCard
import com.nammapustaka.app.ui.components.SectionCard
import com.nammapustaka.app.ui.components.StatusPill
import com.nammapustaka.app.ui.theme.Danger
import com.nammapustaka.app.ui.theme.DangerLight
import com.nammapustaka.app.ui.theme.InkSoft
import com.nammapustaka.app.ui.theme.Leaf
import com.nammapustaka.app.ui.theme.LeafLight
import com.nammapustaka.app.ui.theme.Saffron
import com.nammapustaka.app.ui.theme.SaffronLight
import com.nammapustaka.app.ui.theme.Sky
import com.nammapustaka.app.ui.theme.SkyLight
import com.nammapustaka.app.utils.Formatters
import com.nammapustaka.app.viewmodel.AppUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboardScreen(
    uiState: AppUiState,
    onLogout: () -> Unit,
    onRefresh: suspend () -> Unit,
    onReturnBook: suspend (BorrowRecord) -> Unit,
    onOpenAddBook: () -> Unit,
    onOpenBookQr: (Long) -> Unit,
) {
    if (!uiState.isTeacher) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Teacher Dashboard") })
            },
            containerColor = MaterialTheme.colorScheme.background,
        ) { paddingValues ->
            SectionCard(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "\uD83D\uDD12", fontSize = 42.sp)
                    Text(
                        text = "Teacher access only",
                        modifier = Modifier.padding(top = 12.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                    )
                    Text(
                        text = "Log in as a teacher to add books, scan covers, generate QR codes, and manage returns.\n\nDefault credentials: admin / admin123",
                        modifier = Modifier.padding(top = 8.dp),
                        color = InkSoft,
                        lineHeight = 22.sp,
                    )
                }
            }
        }
        return
    }

    val scope = rememberCoroutineScope()
    val activeRegular = uiState.activeBorrows.filterNot { it.isOverdue }
    val recentBooks = uiState.books.takeLast(8).reversed()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Teacher Dashboard") },
                actions = {
                    IconButton(onClick = { scope.launch { onRefresh() } }) {
                        Icon(Icons.Rounded.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Rounded.Logout, contentDescription = "Logout")
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            item {
                SectionCard(
                    padding = androidx.compose.foundation.layout.PaddingValues(18.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Saffron, RoundedCornerShape(22.dp))
                            .padding(18.dp),
                    ) {
                        Column {
                            Text(
                                text = "Admin Control Center",
                                color = androidx.compose.ui.graphics.Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                            )
                            Text(
                                text = "Teacher login: ${AuthRepository.teacherUsername}",
                                modifier = Modifier.padding(top = 6.dp),
                                color = androidx.compose.ui.graphics.Color(0xFFFDE7D8),
                                fontWeight = FontWeight.Bold,
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 18.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                MetricCard(
                                    label = "Total books",
                                    value = uiState.teacherStats.totalBooks.toString(),
                                    icon = Icons.Rounded.LibraryBooks,
                                    tint = Saffron,
                                    lightTint = SaffronLight,
                                    modifier = Modifier.weight(1f),
                                )
                                MetricCard(
                                    label = "Available",
                                    value = uiState.teacherStats.availableBooks.toString(),
                                    icon = Icons.Rounded.CheckCircleOutline,
                                    tint = Leaf,
                                    lightTint = LeafLight,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                MetricCard(
                                    label = "Borrowed",
                                    value = uiState.teacherStats.borrowedBooks.toString(),
                                    icon = Icons.Rounded.AssignmentTurnedIn,
                                    tint = Sky,
                                    lightTint = SkyLight,
                                    modifier = Modifier.weight(1f),
                                )
                                MetricCard(
                                    label = "Overdue",
                                    value = uiState.teacherStats.overdueBooks.toString(),
                                    icon = Icons.Rounded.WarningAmber,
                                    tint = Danger,
                                    lightTint = DangerLight,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }
                }
            }

            item {
                DashboardSectionTitle("Add Book")
            }
            item {
                SectionCard {
                    Text(
                        text = "Use the camera to capture a cover, auto-fill title and author with OCR, and generate a printable QR code for the new book.",
                        color = InkSoft,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 22.sp,
                    )
                    ElevatedButton(
                        onClick = onOpenAddBook,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp),
                    ) {
                        Icon(Icons.Rounded.AddAPhoto, contentDescription = null)
                        Text("Open Add Book Screen", modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }

            item { DashboardSectionTitle("Overdue Books") }
            if (uiState.overdueBorrows.isEmpty()) {
                item {
                    SectionCard {
                        Text("No overdue books right now.", color = InkSoft, fontWeight = FontWeight.SemiBold)
                    }
                }
            } else {
                items(uiState.overdueBorrows) { borrow ->
                    TeacherBorrowTile(
                        borrow = borrow,
                        overdue = true,
                        onReturn = { scope.launch { onReturnBook(borrow) } },
                    )
                }
            }

            item { DashboardSectionTitle("Process Returns") }
            if (activeRegular.isEmpty()) {
                item {
                    SectionCard {
                        Text("No active non-overdue borrows.", color = InkSoft, fontWeight = FontWeight.SemiBold)
                    }
                }
            } else {
                items(activeRegular) { borrow ->
                    TeacherBorrowTile(
                        borrow = borrow,
                        overdue = false,
                        onReturn = { scope.launch { onReturnBook(borrow) } },
                    )
                }
            }

            item { DashboardSectionTitle("Book QR Codes") }
            if (recentBooks.isEmpty()) {
                item {
                    SectionCard {
                        Text("Add a book to generate its QR code.", color = InkSoft, fontWeight = FontWeight.SemiBold)
                    }
                }
            } else {
                items(recentBooks) { book ->
                    BookQrTile(book = book, onOpen = { onOpenBookQr(book.id) })
                }
            }
        }
    }
}

@Composable
private fun DashboardSectionTitle(title: String) {
    Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Black)
}

@Composable
private fun TeacherBorrowTile(
    borrow: BorrowRecord,
    overdue: Boolean,
    onReturn: () -> Unit,
) {
    SectionCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BookCoverPreview(
                category = borrow.book.category,
                imagePath = borrow.book.imagePath,
                width = 50.dp,
                height = 64.dp,
                iconSize = 24.sp,
                shape = RoundedCornerShape(14.dp),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
            ) {
                Text(text = borrow.book.title, fontSize = 15.sp, fontWeight = FontWeight.Black)
                Text(
                    text = "${borrow.user.name} · Roll No. ${borrow.user.roll ?: "--"}",
                    color = InkSoft,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 4.dp),
                )
                StatusPill(
                    label = if (overdue) {
                        "Overdue · ${Formatters.shortDate(borrow.transaction.dueDateMillis)}"
                    } else {
                        "Due ${Formatters.shortDate(borrow.transaction.dueDateMillis)}"
                    },
                    textColor = if (overdue) Danger else Leaf,
                    backgroundColor = if (overdue) DangerLight else LeafLight,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
            ElevatedButton(onClick = onReturn) {
                Text("Return")
            }
        }
    }
}

@Composable
private fun BookQrTile(book: Book, onOpen: () -> Unit) {
    SectionCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BookCoverPreview(
                category = book.category,
                imagePath = book.imagePath,
                width = 52.dp,
                height = 66.dp,
                iconSize = 26.sp,
                shape = RoundedCornerShape(14.dp),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
            ) {
                Text(text = book.title, fontWeight = FontWeight.Black)
                Text(
                    text = book.author,
                    modifier = Modifier.padding(top = 4.dp),
                    color = InkSoft,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = book.qrValue,
                    modifier = Modifier.padding(top = 6.dp),
                    color = InkSoft,
                    fontSize = 12.sp,
                )
            }
            ElevatedButton(onClick = onOpen) {
                Icon(Icons.Rounded.QrCode2, contentDescription = null)
                Text("QR", modifier = Modifier.padding(start = 6.dp))
            }
        }
    }
}
