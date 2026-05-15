package com.nammapustaka.app.models

data class LeaderboardEntry(
    val user: User,
    val booksRead: Int,
    val pagesRead: Int,
)
