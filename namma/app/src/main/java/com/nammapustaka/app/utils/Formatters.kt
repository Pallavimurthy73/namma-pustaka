package com.nammapustaka.app.utils

import com.nammapustaka.app.models.BorrowRecord
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object Formatters {
    private val shortFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)

    fun shortDate(millis: Long): String {
        return Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(shortFormatter)
    }

    fun dueLabel(borrow: BorrowRecord): String {
        if (borrow.isReturned) return "Returned"

        val daysLeft = borrow.daysLeft
        return when {
            daysLeft < 0 -> "Overdue by ${kotlin.math.abs(daysLeft)} day(s)"
            daysLeft == 0 -> "Due today"
            else -> "Due ${shortDate(borrow.transaction.dueDateMillis)}"
        }
    }
}
