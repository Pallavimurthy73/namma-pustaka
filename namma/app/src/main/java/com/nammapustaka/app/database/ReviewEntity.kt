package com.nammapustaka.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nammapustaka.app.models.Review

@Entity(
    tableName = "reviews",
    indices = [Index(value = ["user_id", "book_id"], unique = true)],
)
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "book_id")
    val bookId: Long,
    val rating: Int,
    val review: String,
)

fun ReviewEntity.toDomain(): Review = Review(
    id = id,
    userId = userId,
    bookId = bookId,
    rating = rating,
    review = review,
)

fun Review.toEntity(): ReviewEntity = ReviewEntity(
    id = id,
    userId = userId,
    bookId = bookId,
    rating = rating,
    review = review,
)
