package com.nammapustaka.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY title ASC")
    fun observeBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :bookId LIMIT 1")
    suspend fun findBookById(bookId: Long): BookEntity?

    @Query("SELECT * FROM books WHERE qr_data = :qrData LIMIT 1")
    suspend fun findBookByQrData(qrData: String): BookEntity?

    @Insert
    suspend fun insert(book: BookEntity): Long

    @Insert
    suspend fun insertAll(books: List<BookEntity>)

    @Query("UPDATE books SET available = :available WHERE id = :bookId")
    suspend fun updateAvailability(bookId: Long, available: Boolean)

    @Query("SELECT COUNT(*) FROM books")
    suspend fun countBooks(): Int
}
