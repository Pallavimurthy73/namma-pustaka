package com.nammapustaka.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TopLevelDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
) {
    data object Library : TopLevelDestination(
        route = "library",
        label = "Library",
        icon = Icons.Outlined.MenuBook,
        selectedIcon = Icons.Rounded.MenuBook,
    )

    data object Scanner : TopLevelDestination(
        route = "scanner",
        label = "QR Scan",
        icon = Icons.Outlined.QrCodeScanner,
        selectedIcon = Icons.Rounded.QrCodeScanner,
    )

    data object Leaderboard : TopLevelDestination(
        route = "leaderboard",
        label = "Leaderboard",
        icon = Icons.Outlined.EmojiEvents,
        selectedIcon = Icons.Rounded.EmojiEvents,
    )

    data object TeacherDashboard : TopLevelDestination(
        route = "teacher",
        label = "Teacher Dashboard",
        icon = Icons.Outlined.Dashboard,
        selectedIcon = Icons.Rounded.Dashboard,
    )
}

object AppDestination {
    const val BOOK_DETAIL = "bookDetail/{bookId}"
    const val ADD_BOOK = "addBook"
    const val BOOK_QR = "bookQr/{bookId}/{showSuccess}"

    fun bookDetail(bookId: Long) = "bookDetail/$bookId"
    fun bookQr(bookId: Long, showSuccess: Boolean = false) = "bookQr/$bookId/$showSuccess"
}

val topLevelDestinations = listOf(
    TopLevelDestination.Library,
    TopLevelDestination.Scanner,
    TopLevelDestination.Leaderboard,
    TopLevelDestination.TeacherDashboard,
)
