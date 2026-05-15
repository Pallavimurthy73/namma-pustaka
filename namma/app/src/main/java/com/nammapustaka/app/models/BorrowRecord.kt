package com.nammapustaka.app.models

import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

data class BorrowRecord(
    val book: Book,
    val user: User,
    val transaction: BorrowTransaction,
) {
    val isReturned: Boolean
        get() = transaction.returned

    val isOverdue: Boolean
        get() = !isReturned && transaction.dueDateMillis < System.currentTimeMillis()

    val daysLeft: Int
        get() {
            val zoneId = ZoneId.systemDefault()
            val today = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(zoneId).toLocalDate()
            val dueDate = Instant.ofEpochMilli(transaction.dueDateMillis).atZone(zoneId).toLocalDate()
            return ChronoUnit.DAYS.between(today, dueDate).toInt()
        }
}
