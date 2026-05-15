package com.nammapustaka.app.utils

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.nammapustaka.app.models.OcrBookSuggestion
import kotlinx.coroutines.tasks.await
import java.io.File

class OcrService {
    suspend fun scanBookCover(context: Context, imagePath: String): OcrBookSuggestion {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        return try {
            val inputImage = InputImage.fromFilePath(context, Uri.fromFile(File(imagePath)))
            val recognizedText = recognizer.process(inputImage).await()
            parseRecognizedText(recognizedText)
        } finally {
            recognizer.close()
        }
    }

    private fun parseRecognizedText(recognizedText: com.google.mlkit.vision.text.Text): OcrBookSuggestion {
        val parsedLines = mutableListOf<ParsedLine>()

        recognizedText.textBlocks.forEach { block ->
            block.lines.forEachIndexed { index, line ->
                val cleaned = cleanLine(line.text)
                val box = line.boundingBox
                if (cleaned.isBlank() || isNoise(cleaned) || box == null) {
                    return@forEachIndexed
                }
                parsedLines += ParsedLine(
                    text = cleaned,
                    top = box.top.toFloat(),
                    height = box.height().toFloat(),
                    order = parsedLines.size + index,
                )
            }
        }

        parsedLines.sortWith(compareBy<ParsedLine> { it.top }.thenBy { it.order })
        val titleLine = findTitleLine(parsedLines)
        val author = findAuthor(parsedLines, titleLine)

        return OcrBookSuggestion(
            title = titleLine?.text,
            author = author,
            rawText = recognizedText.text.trim(),
        )
    }

    private fun findTitleLine(lines: List<ParsedLine>): ParsedLine? {
        if (lines.isEmpty()) return null

        val candidates = lines
            .filterNot { isMetadata(it.text) || looksLikeAuthorOnly(it.text) }
            .take(5)

        if (candidates.isEmpty()) return lines.first()
        return candidates.maxByOrNull(::titleScore)
    }

    private fun titleScore(line: ParsedLine): Double {
        val wordCount = line.text.split(' ').count { it.isNotBlank() }
        val lengthBonus = if (line.text.length >= 6) 6.0 else line.text.length.toDouble()
        val topPenalty = line.order * 1.5
        return (line.height * 3) + wordCount + lengthBonus - topPenalty
    }

    private fun findAuthor(lines: List<ParsedLine>, titleLine: ParsedLine?): String? {
        lines.forEach { line ->
            val prefix = Regex("^\\s*by\\s+", RegexOption.IGNORE_CASE).find(line.text)
            if (prefix != null) {
                val author = cleanLine(line.text.substring(prefix.range.last + 1))
                if (author.isNotBlank() && !isMetadata(author)) return author
            }
        }

        val titleIndex = titleLine?.let(lines::indexOf) ?: -1
        if (titleIndex >= 0) {
            lines.drop(titleIndex + 1).forEach { line ->
                if (!isMetadata(line.text) && line.text != titleLine?.text && looksLikeAuthorOnly(line.text)) {
                    return line.text
                }
            }
        }

        return lines.firstOrNull { looksLikeAuthorOnly(it.text) && it.text != titleLine?.text }?.text
    }

    private fun looksLikeAuthorOnly(text: String): Boolean {
        val lower = text.lowercase()
        if (lower.startsWith("by ")) return true
        if (isMetadata(text)) return false

        val words = text.split(' ').filter { it.isNotBlank() }
        if (words.size !in 2..5) return false
        return !text.any(Char::isDigit)
    }

    private fun isNoise(text: String): Boolean {
        if (text.length < 2) return true
        val lettersOnly = text.replace(Regex("[^A-Za-z]"), "")
        val digitsOnly = text.replace(Regex("[^0-9]"), "")
        if (digitsOnly.isNotEmpty() && digitsOnly.length >= text.length - 1) return true
        return lettersOnly.isEmpty() && digitsOnly.isEmpty()
    }

    private fun isMetadata(text: String): Boolean {
        val lower = text.lowercase()
        return listOf(
            "isbn",
            "edition",
            "price",
            "rs",
            "mrp",
            "pages",
            "published",
            "publication",
            "volume",
        ).any(lower::contains)
    }

    private fun cleanLine(input: String): String {
        return input
            .replace(Regex("[\\u0000-\\u001F]"), " ")
            .replace(Regex("[|~`*_<>]+"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()
            .replace(Regex("^[\\s\\.,:;!?\\-_/\\\\()\"']+|[\\s\\.,:;!?\\-_/\\\\()\"']+$"), "")
    }

    private data class ParsedLine(
        val text: String,
        val top: Float,
        val height: Float,
        val order: Int,
    )
}
