package com.nammapustaka.app.models

data class TeacherStats(
    val totalBooks: Int = 0,
    val availableBooks: Int = 0,
    val borrowedBooks: Int = 0,
    val overdueBooks: Int = 0,
)
