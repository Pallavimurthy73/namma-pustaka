package com.nammapustaka.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BorrowTransactionDao {
    @Insert
    suspend fun insert(transaction: BorrowTransactionEntity): Long

    @Insert
    suspend fun insertAll(transactions: List<BorrowTransactionEntity>)

    @Query("SELECT EXISTS(SELECT 1 FROM transactions WHERE book_id = :bookId AND returned = 0)")
    suspend fun hasActiveBorrow(bookId: Long): Boolean

    @Query("UPDATE transactions SET returned = 1 WHERE id = :transactionId")
    suspend fun markReturned(transactionId: Long)

    @Query(
        """
        SELECT
            t.id AS transaction_id,
            t.user_id,
            t.book_id,
            t.issue_date,
            t.due_date,
            t.returned,
            u.name AS user_name,
            u.role AS user_role,
            u.roll AS user_roll,
            u.pin AS user_pin,
            b.title AS book_title,
            b.author AS book_author,
            b.category AS book_category,
            b.pages AS book_pages,
            b.summary AS book_summary,
            b.available AS book_available,
            b.image_path AS book_image_path,
            b.qr_data AS book_qr_data
        FROM transactions t
        INNER JOIN users u ON u.id = t.user_id
        INNER JOIN books b ON b.id = t.book_id
        ORDER BY t.returned ASC, t.due_date ASC, t.issue_date DESC
        """,
    )
    fun observeAllBorrowRecords(): Flow<List<BorrowRecordRow>>

    @Query(
        """
        SELECT
            t.id AS transaction_id,
            t.user_id,
            t.book_id,
            t.issue_date,
            t.due_date,
            t.returned,
            u.name AS user_name,
            u.role AS user_role,
            u.roll AS user_roll,
            u.pin AS user_pin,
            b.title AS book_title,
            b.author AS book_author,
            b.category AS book_category,
            b.pages AS book_pages,
            b.summary AS book_summary,
            b.available AS book_available,
            b.image_path AS book_image_path,
            b.qr_data AS book_qr_data
        FROM transactions t
        INNER JOIN users u ON u.id = t.user_id
        INNER JOIN books b ON b.id = t.book_id
        WHERE t.returned = :returned
        ORDER BY t.returned ASC, t.due_date ASC, t.issue_date DESC
        """,
    )
    fun observeBorrowRecordsByReturned(returned: Boolean): Flow<List<BorrowRecordRow>>
}
