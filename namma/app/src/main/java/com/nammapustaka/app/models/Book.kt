package com.nammapustaka.app.models

data class Book(
    val id: Long = 0L,
    val title: String,
    val author: String,
    val category: String,
    val pages: Int,
    val summary: String,
    val available: Boolean,
    val imagePath: String? = null,
    val qrData: String? = null,
) {
    val hasCoverImage: Boolean
        get() = !imagePath.isNullOrBlank()

    val qrValue: String
        get() = qrData?.takeIf { it.isNotBlank() } ?: "book:$id"
}
