package com.nammapustaka.app.viewmodel

import com.nammapustaka.app.models.Book
import com.nammapustaka.app.models.BorrowRecord
import com.nammapustaka.app.models.LeaderboardEntry
import com.nammapustaka.app.models.Review
import com.nammapustaka.app.models.TeacherStats
import com.nammapustaka.app.models.User
import com.nammapustaka.app.utils.BookCategories

data class AppUiState(
    val isLoading: Boolean = false,
    val currentUser: User? = null,
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val books: List<Book> = emptyList(),
    val myBorrowRecords: List<BorrowRecord> = emptyList(),
    val activeBorrows: List<BorrowRecord> = emptyList(),
    val overdueBorrows: List<BorrowRecord> = emptyList(),
    val leaderboard: List<LeaderboardEntry> = emptyList(),
    val teacherStats: TeacherStats = TeacherStats(),
    val reviewsByBook: Map<Long, List<Review>> = emptyMap(),
) {
    val isLoggedIn: Boolean
        get() = currentUser != null

    val isTeacher: Boolean
        get() = currentUser?.isTeacher == true

    val isStudent: Boolean
        get() = currentUser?.isStudent == true

    val categories: List<String>
        get() = BookCategories.all

    val filteredBooks: List<Book>
        get() {
            val query = searchQuery.trim().lowercase()
            return books.filter { book ->
                val matchesCategory = selectedCategory == "All" || book.category == selectedCategory
                val matchesSearch = query.isBlank() ||
                    book.title.lowercase().contains(query) ||
                    book.author.lowercase().contains(query)
                matchesCategory && matchesSearch
            }
        }

    val myActiveBorrows: List<BorrowRecord>
        get() = myBorrowRecords.filterNot { it.isReturned }

    val myReturnedBorrows: List<BorrowRecord>
        get() = myBorrowRecords.filter { it.isReturned }

    val booksRead: Int
        get() = myReturnedBorrows.size

    val pagesRead: Int
        get() = myReturnedBorrows.sumOf { it.book.pages }

    val activeBorrowCount: Int
        get() = myActiveBorrows.size

    val goalProgress: Float
        get() = (pagesRead / 200f).coerceAtMost(1f)

    val studentRank: Int
        get() {
            val id = currentUser?.id ?: return 0
            return leaderboard.indexOfFirst { it.user.id == id }.let { index ->
                if (index >= 0) index + 1 else 0
            }
        }

    fun reviewsForBook(bookId: Long): List<Review> = reviewsByBook[bookId].orEmpty()

    fun averageRatingForBook(bookId: Long): Double {
        val reviews = reviewsForBook(bookId)
        if (reviews.isEmpty()) return 0.0
        return reviews.sumOf { it.rating } / reviews.size.toDouble()
    }

    fun reviewCountForBook(bookId: Long): Int = reviewsForBook(bookId).size

    val hasAnyUserReview: Boolean
        get() {
            val userId = currentUser?.id ?: return false
            return reviewsByBook.values.flatten().any { it.userId == userId }
        }
}
