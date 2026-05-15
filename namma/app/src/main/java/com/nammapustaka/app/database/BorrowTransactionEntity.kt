package com.nammapustaka.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nammapustaka.app.models.BorrowTransaction

@Entity(tableName = "transactions")
data class BorrowTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "book_id")
    val bookId: Long,
    @ColumnInfo(name = "issue_date")
    val issueDateMillis: Long,
    @ColumnInfo(name = "due_date")
    val dueDateMillis: Long,
    val returned: Boolean = false,
)

fun BorrowTransactionEntity.toDomain(): BorrowTransaction = BorrowTransaction(
    id = id,
    userId = userId,
    bookId = bookId,
    issueDateMillis = issueDateMillis,
    dueDateMillis = dueDateMillis,
    returned = returned,
)

fun BorrowTransaction.toEntity(): BorrowTransactionEntity = BorrowTransactionEntity(
    id = id,
    userId = userId,
    bookId = bookId,
    issueDateMillis = issueDateMillis,
    dueDateMillis = dueDateMillis,
    returned = returned,
)
