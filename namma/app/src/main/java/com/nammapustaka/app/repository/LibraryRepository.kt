package com.nammapustaka.app.repository

import androidx.room.withTransaction
import com.nammapustaka.app.database.BorrowTransactionEntity
import com.nammapustaka.app.database.NammaPustakaDatabase
import com.nammapustaka.app.database.ReviewEntity
import com.nammapustaka.app.database.SeedData
import com.nammapustaka.app.database.toDomain
import com.nammapustaka.app.database.toEntity
import com.nammapustaka.app.models.Book
import com.nammapustaka.app.models.BorrowRecord
import com.nammapustaka.app.models.LeaderboardEntry
import com.nammapustaka.app.models.Review
import com.nammapustaka.app.models.TeacherStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.util.concurrent.atomic.AtomicLong

class LibraryRepository(
    private val database: NammaPustakaDatabase,
) {
    private val userDao = database.userDao()
    private val bookDao = database.bookDao()
    private val transactionDao = database.borrowTransactionDao()
    private val reviewDao = database.reviewDao()
    private val qrSeed = AtomicLong(System.currentTimeMillis() * 1000L)

    val booksFlow: Flow<List<Book>> = bookDao.observeBooks().map { rows -> rows.map { it.toDomain() } }

    val allBorrowRecordsFlow: Flow<List<BorrowRecord>> =
        transactionDao.observeAllBorrowRecords().map { rows -> rows.map { it.toDomain() } }

    val activeBorrowsFlow: Flow<List<BorrowRecord>> =
        transactionDao.observeBorrowRecordsByReturned(false).map { rows -> rows.map { it.toDomain() } }

    val reviewsFlow: Flow<List<Review>> =
        reviewDao.observeAllReviews().map { rows -> rows.map { it.toDomain() } }

    val overdueBorrowsFlow: Flow<List<BorrowRecord>> =
        activeBorrowsFlow.map { borrows -> borrows.filter { it.isOverdue } }

    val teacherStatsFlow: Flow<TeacherStats> =
        combine(booksFlow, activeBorrowsFlow) { books, activeBorrows ->
            TeacherStats(
                totalBooks = books.size,
                availableBooks = books.count { it.available },
                borrowedBooks = activeBorrows.size,
                overdueBooks = activeBorrows.count { it.isOverdue },
            )
        }

    val leaderboardFlow: Flow<List<LeaderboardEntry>> =
        combine(
            userDao.observeStudents().map { rows -> rows.map { it.toDomain() } },
            allBorrowRecordsFlow,
        ) { students, records ->
            val returnedBorrows = records.filter { it.isReturned }
            val scores = returnedBorrows.groupBy { it.user.id }.mapValues { (_, borrows) ->
                val booksRead = borrows.size
                val pagesRead = borrows.sumOf { it.book.pages }
                booksRead to pagesRead
            }

            students.map { student ->
                val score = scores[student.id] ?: (0 to 0)
                LeaderboardEntry(
                    user = student,
                    booksRead = score.first,
                    pagesRead = score.second,
                )
            }.sortedWith(
                compareByDescending<LeaderboardEntry> { it.pagesRead }
                    .thenByDescending { it.booksRead }
                    .thenBy { it.user.name },
            )
        }

    suspend fun ensureSeedData() {
        if (userDao.countUsers() > 0 || bookDao.countBooks() > 0) {
            return
        }

        database.withTransaction {
            userDao.insertAll(SeedData.users)
            bookDao.insertAll(SeedData.books)
            transactionDao.insertAll(SeedData.transactions(System.currentTimeMillis()))
            reviewDao.insertAll(SeedData.reviews)
        }
    }

    suspend fun addBook(book: Book): Book {
        val id = bookDao.insert(book.toEntity())
        return book.copy(id = id)
    }

    suspend fun findBookById(bookId: Long): Book? = bookDao.findBookById(bookId)?.toDomain()

    suspend fun borrowBookFromQr(userId: Long, qrValue: String): String? {
        val normalized = qrValue.trim()
        if (normalized.isBlank()) {
            return "Please scan or enter a valid QR code."
        }

        var book = bookDao.findBookByQrData(normalized)?.toDomain()
        if (book == null && normalized.startsWith("book:", ignoreCase = true)) {
            val id = normalized.substringAfterLast(':').toLongOrNull()
            if (id != null) {
                book = findBookById(id)
            }
        }

        if (book == null || book.id == 0L) {
            return "This QR code is not linked to a book in the library."
        }

        return borrowBook(userId = userId, bookId = book.id)
    }

    suspend fun borrowBook(userId: Long, bookId: Long): String? {
        val book = findBookById(bookId)
            ?: return "This book is not available in the library."

        if (!book.available || transactionDao.hasActiveBorrow(bookId)) {
            return "This book is already issued."
        }

        val issueDate = System.currentTimeMillis()
        val dueDate = issueDate + Duration.ofDays(14).toMillis()

        database.withTransaction {
            transactionDao.insert(
                BorrowTransactionEntity(
                    userId = userId,
                    bookId = bookId,
                    issueDateMillis = issueDate,
                    dueDateMillis = dueDate,
                    returned = false,
                ),
            )
            bookDao.updateAvailability(bookId, false)
        }
        return null
    }

    suspend fun returnBook(borrow: BorrowRecord) {
        database.withTransaction {
            transactionDao.markReturned(borrow.transaction.id)
            bookDao.updateAvailability(borrow.book.id, true)
        }
    }

    suspend fun submitReview(userId: Long, bookId: Long, rating: Int, review: String) {
        reviewDao.upsert(
            ReviewEntity(
                userId = userId,
                bookId = bookId,
                rating = rating,
                review = review.trim(),
            ),
        )
    }

    fun generateBookQrData(): String {
        return "BOOK_${qrSeed.incrementAndGet()}"
    }
}
