package com.nammapustaka.app.models

data class BorrowTransaction(
    val id: Long = 0L,
    val userId: Long,
    val bookId: Long,
    val issueDateMillis: Long,
    val dueDateMillis: Long,
    val returned: Boolean,
)
