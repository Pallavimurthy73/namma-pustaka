package com.nammapustaka.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nammapustaka.app.models.Book

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val author: String,
    val category: String,
    val pages: Int,
    val summary: String,
    val available: Boolean = true,
    @ColumnInfo(name = "image_path")
    val imagePath: String? = null,
    @ColumnInfo(name = "qr_data")
    val qrData: String? = null,
)

fun BookEntity.toDomain(): Book = Book(
    id = id,
    title = title,
    author = author,
    category = category,
    pages = pages,
    summary = summary,
    available = available,
    imagePath = imagePath,
    qrData = qrData,
)

fun Book.toEntity(): BookEntity = BookEntity(
    id = id,
    title = title,
    author = author,
    category = category,
    pages = pages,
    summary = summary,
    available = available,
    imagePath = imagePath,
    qrData = qrData,
)
