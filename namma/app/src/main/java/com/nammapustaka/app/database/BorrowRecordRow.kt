package com.nammapustaka.app.database

import androidx.room.ColumnInfo
import com.nammapustaka.app.models.Book
import com.nammapustaka.app.models.BorrowRecord
import com.nammapustaka.app.models.BorrowTransaction
import com.nammapustaka.app.models.User

data class BorrowRecordRow(
    @ColumnInfo(name = "transaction_id")
    val transactionId: Long,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "book_id")
    val bookId: Long,
    @ColumnInfo(name = "issue_date")
    val issueDateMillis: Long,
    @ColumnInfo(name = "due_date")
    val dueDateMillis: Long,
    val returned: Boolean,
    @ColumnInfo(name = "user_name")
    val userName: String,
    @ColumnInfo(name = "user_role")
    val userRole: String,
    @ColumnInfo(name = "user_roll")
    val userRoll: String?,
    @ColumnInfo(name = "user_pin")
    val userPin: String,
    @ColumnInfo(name = "book_title")
    val bookTitle: String,
    @ColumnInfo(name = "book_author")
    val bookAuthor: String,
    @ColumnInfo(name = "book_category")
    val bookCategory: String,
    @ColumnInfo(name = "book_pages")
    val bookPages: Int,
    @ColumnInfo(name = "book_summary")
    val bookSummary: String,
    @ColumnInfo(name = "book_available")
    val bookAvailable: Boolean,
    @ColumnInfo(name = "book_image_path")
    val bookImagePath: String?,
    @ColumnInfo(name = "book_qr_data")
    val bookQrData: String?,
)

fun BorrowRecordRow.toDomain(): BorrowRecord = BorrowRecord(
    transaction = BorrowTransaction(
        id = transactionId,
        userId = userId,
        bookId = bookId,
        issueDateMillis = issueDateMillis,
        dueDateMillis = dueDateMillis,
        returned = returned,
    ),
    user = User(
        id = userId,
        name = userName,
        role = userRole,
        roll = userRoll,
        pin = userPin,
    ),
    book = Book(
        id = bookId,
        title = bookTitle,
        author = bookAuthor,
        category = bookCategory,
        pages = bookPages,
        summary = bookSummary,
        available = bookAvailable,
        imagePath = bookImagePath,
        qrData = bookQrData,
    ),
)
