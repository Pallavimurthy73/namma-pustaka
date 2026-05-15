package com.nammapustaka.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Print
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.nammapustaka.app.ui.components.BookCoverPreview
import com.nammapustaka.app.ui.components.SectionCard
import com.nammapustaka.app.ui.theme.Ink
import com.nammapustaka.app.ui.theme.InkSoft
import com.nammapustaka.app.ui.theme.Leaf
import com.nammapustaka.app.ui.theme.LeafLight
import com.nammapustaka.app.ui.theme.SaffronDark
import com.nammapustaka.app.ui.theme.SaffronLight
import com.nammapustaka.app.utils.QrCodeGenerator
import com.nammapustaka.app.utils.QrPrintHelper
import com.nammapustaka.app.viewmodel.AppUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookQrScreen(
    uiState: AppUiState,
    bookId: Long,
    showSuccessMessage: Boolean,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val book = uiState.books.firstOrNull { it.id == bookId }

    LaunchedEffect(showSuccessMessage) {
        if (showSuccessMessage) {
            snackbarHostState.showSnackbar("Book saved successfully. The QR below is ready to print or save as PDF.")
        }
    }

    if (book == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Book QR") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                        }
                    },
                )
            },
        ) { paddingValues ->
            SectionCard(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
                Text("The selected book could not be found.")
            }
        }
        return
    }

    val qrBitmap = remember(book.qrValue) { QrCodeGenerator.generateBitmap(book.qrValue, 720).asImageBitmap() }
    val printHelper = remember(context) { QrPrintHelper(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book QR") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (showSuccessMessage) {
                SectionCard {
                    Text(
                        text = "Book saved successfully. The QR below is ready to print or save as PDF.",
                        color = Leaf,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp,
                    )
                }
            }

            SectionCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BookCoverPreview(
                        category = book.category,
                        imagePath = book.imagePath,
                        width = 88.dp,
                        height = 114.dp,
                        iconSize = 42.sp,
                    )
                    Column(modifier = Modifier.padding(start = 14.dp)) {
                        Text(text = book.title, fontSize = 20.sp, fontWeight = FontWeight.Black, color = Ink)
                        Text(
                            text = book.author,
                            modifier = Modifier.padding(top = 6.dp),
                            color = InkSoft,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = book.category,
                            modifier = Modifier.padding(top = 12.dp),
                            color = SaffronDark,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "${book.pages} pages",
                            color = SaffronDark,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            SectionCard {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "QR Preview", fontSize = 20.sp, fontWeight = FontWeight.Black)
                    Image(
                        bitmap = qrBitmap,
                        contentDescription = "QR code",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                    )
                    Text(
                        text = book.qrValue,
                        modifier = Modifier.padding(top = 14.dp),
                        color = InkSoft,
                        fontWeight = FontWeight.Bold,
                    )
                    ElevatedButton(
                        onClick = { printHelper.printBookQr(book) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 18.dp),
                    ) {
                        Icon(Icons.Rounded.Print, contentDescription = null)
                        Text("Print QR", modifier = Modifier.padding(start = 8.dp))
                    }
                    Text(
                        text = "The system print sheet also lets you save the QR as a PDF.",
                        modifier = Modifier.padding(top = 8.dp),
                        color = InkSoft,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}
