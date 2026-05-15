package com.nammapustaka.app.models

data class OcrBookSuggestion(
    val title: String? = null,
    val author: String? = null,
    val rawText: String,
) {
    val hasSuggestion: Boolean
        get() = !title.isNullOrBlank() || !author.isNullOrBlank()
}
