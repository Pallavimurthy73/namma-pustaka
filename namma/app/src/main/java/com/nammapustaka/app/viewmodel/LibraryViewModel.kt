package com.nammapustaka.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammapustaka.app.models.Book
import com.nammapustaka.app.models.BorrowRecord
import com.nammapustaka.app.models.Review
import com.nammapustaka.app.models.User
import com.nammapustaka.app.repository.AppContainer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val appContainer: AppContainer,
) : ViewModel() {
    private val authRepository = appContainer.authRepository
    private val libraryRepository = appContainer.libraryRepository

    private val loading = MutableStateFlow(true)
    private val currentUser = MutableStateFlow<User?>(null)
    private val searchQuery = MutableStateFlow("")
    private val selectedCategory = MutableStateFlow("All")

    val uiState: StateFlow<AppUiState> = combine(
        loading,
        currentUser,
        searchQuery,
        selectedCategory,
        libraryRepository.booksFlow,
        libraryRepository.allBorrowRecordsFlow,
        libraryRepository.activeBorrowsFlow,
        libraryRepository.overdueBorrowsFlow,
        libraryRepository.leaderboardFlow,
        libraryRepository.teacherStatsFlow,
        libraryRepository.reviewsFlow,
    ) { values ->
        val isLoading = values[0] as Boolean
        val user = values[1] as User?
        val query = values[2] as String
        val category = values[3] as String
        val books = values[4] as List<Book>
        val allBorrows = values[5] as List<BorrowRecord>
        val activeBorrows = values[6] as List<BorrowRecord>
        val overdueBorrows = values[7] as List<BorrowRecord>
        @Suppress("UNCHECKED_CAST")
        val leaderboard = values[8] as List<com.nammapustaka.app.models.LeaderboardEntry>
        val teacherStats = values[9] as com.nammapustaka.app.models.TeacherStats
        val reviews = values[10] as List<Review>

        AppUiState(
            isLoading = isLoading,
            currentUser = user,
            searchQuery = query,
            selectedCategory = category,
            books = books,
            myBorrowRecords = if (user?.isStudent == true) {
                allBorrows.filter { it.user.id == user.id }
            } else {
                emptyList()
            },
            activeBorrows = activeBorrows,
            overdueBorrows = overdueBorrows,
            leaderboard = leaderboard,
            teacherStats = teacherStats,
            reviewsByBook = reviews.groupBy { it.bookId },
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppUiState(isLoading = true),
    )

    init {
        viewModelScope.launch {
            libraryRepository.ensureSeedData()
            loading.value = false
        }
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun setCategory(category: String) {
        selectedCategory.value = category
    }

    suspend fun refresh() {
        loading.value = true
        delay(180)
        loading.value = false
    }

    suspend fun loginStudent(name: String, roll: String, pin: String): String? {
        loading.value = true
        val user = authRepository.loginStudent(name, roll, pin)
        loading.value = false
        return if (user == null) {
            "Name, roll number, or PIN is incorrect."
        } else {
            currentUser.value = user
            null
        }
    }

    suspend fun registerStudent(name: String, roll: String, pin: String): String? {
        loading.value = true
        val user = authRepository.registerStudent(name, roll, pin)
        loading.value = false
        return if (user == null) {
            "That roll number is already registered."
        } else {
            currentUser.value = user
            null
        }
    }

    suspend fun loginTeacher(username: String, password: String): String? {
        loading.value = true
        val user = authRepository.loginTeacher(username, password)
        loading.value = false
        return if (user == null) {
            "Teacher login failed. Use admin / admin123."
        } else {
            currentUser.value = user
            null
        }
    }

    fun logout() {
        currentUser.value = null
        searchQuery.value = ""
        selectedCategory.value = "All"
    }

    suspend fun borrowBookFromQr(qrValue: String): String? {
        val userId = currentUser.value?.id ?: 0L
        if (userId == 0L || currentUser.value?.isStudent != true) {
            return "Log in as a student to borrow a book."
        }
        return libraryRepository.borrowBookFromQr(userId, qrValue)
    }

    suspend fun addBook(
        title: String,
        author: String,
        category: String,
        pages: Int,
        summary: String,
        imagePath: String?,
    ): Book? {
        if (currentUser.value?.isTeacher != true) return null

        return libraryRepository.addBook(
            Book(
                title = title.trim(),
                author = author.trim(),
                category = category,
                pages = pages,
                summary = summary.trim(),
                available = true,
                imagePath = imagePath?.trim()?.takeIf { it.isNotBlank() },
                qrData = libraryRepository.generateBookQrData(),
            ),
        )
    }

    suspend fun returnBorrowedBook(borrow: BorrowRecord) {
        libraryRepository.returnBook(borrow)
    }

    suspend fun submitReview(bookId: Long, rating: Int, review: String): String? {
        val userId = currentUser.value?.id ?: 0L
        if (currentUser.value?.isStudent != true || userId == 0L) {
            return "Log in as a student before leaving a review."
        }
        if (rating !in 1..5) {
            return "Please choose a rating between 1 and 5 stars."
        }
        libraryRepository.submitReview(userId, bookId, rating, review)
        return null
    }

    fun findBookById(bookId: Long): Book? = uiState.value.books.firstOrNull { it.id == bookId }
}
