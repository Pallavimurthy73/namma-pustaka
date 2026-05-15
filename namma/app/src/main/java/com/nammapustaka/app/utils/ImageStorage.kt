package com.nammapustaka.app.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object ImageStorage {
    data class CaptureTarget(
        val file: File,
        val uri: Uri,
    )

    fun createCaptureTarget(context: Context): CaptureTarget {
        val directory = File(context.cacheDir, "images").apply { mkdirs() }
        val file = File(directory, "capture_${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file,
        )
        return CaptureTarget(file = file, uri = uri)
    }

    fun persistCapturedImage(context: Context, sourceFile: File): String {
        val destinationDirectory = File(context.filesDir, "book_covers").apply { mkdirs() }
        val destination = File(
            destinationDirectory,
            "cover_${System.currentTimeMillis()}.jpg",
        )
        sourceFile.copyTo(destination, overwrite = true)
        return destination.absolutePath
    }
}
