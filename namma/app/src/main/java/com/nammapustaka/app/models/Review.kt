package com.nammapustaka.app.models

data class Review(
    val id: Long = 0L,
    val userId: Long,
    val bookId: Long,
    val rating: Int,
    val review: String,
)
