package com.nammapustaka.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import com.nammapustaka.app.models.Book
import java.io.FileOutputStream

class QrPrintHelper(
    private val context: Context,
) {
    fun printBookQr(book: Book) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        printManager.print(
            "${book.title}_qr",
            BookQrPrintAdapter(book = book),
            null,
        )
    }

    private inner class BookQrPrintAdapter(
        private val book: Book,
    ) : PrintDocumentAdapter() {
        override fun onLayout(
            oldAttributes: PrintAttributes?,
            newAttributes: PrintAttributes,
            cancellationSignal: CancellationSignal?,
            callback: LayoutResultCallback,
            extras: Bundle?,
        ) {
            if (cancellationSignal?.isCanceled == true) {
                callback.onLayoutCancelled()
                return
            }

            callback.onLayoutFinished(
                PrintDocumentInfo.Builder("${book.title}_qr.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(1)
                    .build(),
                true,
            )
        }

        override fun onWrite(
            pages: Array<out PageRange>,
            destination: ParcelFileDescriptor,
            cancellationSignal: CancellationSignal?,
            callback: WriteResultCallback,
        ) {
            val document = PdfDocument()
            try {
                val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
                val page = document.startPage(pageInfo)
                drawPage(page.canvas, book, QrCodeGenerator.generateBitmap(book.qrValue, 420))
                document.finishPage(page)

                FileOutputStream(destination.fileDescriptor).use { output ->
                    document.writeTo(output)
                }
                callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
            } catch (_: Exception) {
                callback.onWriteFailed("Unable to generate QR PDF.")
            } finally {
                document.close()
            }
        }

        private fun drawPage(canvas: Canvas, book: Book, qrBitmap: Bitmap) {
            val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 34f
                isFakeBoldText = true
            }
            val subtitlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 22f
            }
            val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 18f
            }

            canvas.drawText(book.title, 70f, 120f, titlePaint)
            canvas.drawText(book.author, 70f, 160f, subtitlePaint)

            val qrLeft = 88f
            val qrTop = 210f
            canvas.drawBitmap(qrBitmap, qrLeft, qrTop, null)
            canvas.drawText(book.qrValue, 70f, 690f, bodyPaint)
            canvas.drawText("Namma Pustaka QR", 70f, 730f, subtitlePaint)
        }
    }
}
