package com.nammapustaka.app.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammapustaka.app.models.Book
import com.nammapustaka.app.ui.components.BookCoverPreview
import com.nammapustaka.app.ui.components.SectionCard
import com.nammapustaka.app.ui.theme.Danger
import com.nammapustaka.app.ui.theme.DangerLight
import com.nammapustaka.app.ui.theme.Ink
import com.nammapustaka.app.ui.theme.InkSoft
import com.nammapustaka.app.ui.theme.Leaf
import com.nammapustaka.app.ui.theme.LeafLight
import com.nammapustaka.app.ui.theme.SaffronLight
import com.nammapustaka.app.utils.BookCategories
import com.nammapustaka.app.utils.ImageStorage
import com.nammapustaka.app.utils.OcrService
import com.nammapustaka.app.viewmodel.AppUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    uiState: AppUiState,
    onBack: () -> Unit,
    onAddBook: suspend (String, String, String, Int, String, String?) -> Book?,
    onBookSaved: (Long) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val ocrService = remember { OcrService() }

    var title by rememberSaveable { mutableStateOf("") }
    var author by rememberSaveable { mutableStateOf("") }
    var pages by rememberSaveable { mutableStateOf("") }
    var summary by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("Story") }
    var imagePath by rememberSaveable { mutableStateOf<String?>(null) }
    var isProcessingOcr by rememberSaveable { mutableStateOf(false) }
    var isSaving by rememberSaveable { mutableStateOf(false) }
    var ocrMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var ocrIsError by rememberSaveable { mutableStateOf(false) }
    var captureTarget by remember { mutableStateOf<ImageStorage.CaptureTarget?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success ->
        val target = captureTarget ?: return@rememberLauncherForActivityResult
        if (!success) return@rememberLauncherForActivityResult

        scope.launch {
            try {
                isProcessingOcr = true
                ocrMessage = null
                val storedPath = ImageStorage.persistCapturedImage(context, target.file)
                imagePath = storedPath
                val suggestion = ocrService.scanBookCover(context, storedPath)
                if (!suggestion.title.isNullOrBlank()) title = suggestion.title
                if (!suggestion.author.isNullOrBlank()) author = suggestion.author
                if (!suggestion.hasSuggestion) {
                    ocrIsError = true
                    ocrMessage = "OCR could not confidently detect the title and author. You can type them manually."
                } else {
                    ocrIsError = false
                    ocrMessage = "Auto-filled detected details from the captured cover. You can edit them before saving."
                }
            } catch (_: Exception) {
                ocrIsError = true
                ocrMessage = "Book cover captured, but OCR could not be completed. Please enter the details manually."
            } finally {
                isProcessingOcr = false
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            val target = ImageStorage.createCaptureTarget(context)
            captureTarget = target
            takePictureLauncher.launch(target.uri)
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("Camera permission is required to capture a book cover.")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Book") },
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
            SectionCard {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Text(
                            text = "Scan the cover, review the OCR, then save the book.",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Ink,
                        )
                        Text(
                            text = "Captured cover images are stored locally and the detected title and author remain editable before the book is added.",
                            modifier = Modifier.padding(top = 8.dp),
                            color = InkSoft,
                            lineHeight = 22.sp,
                        )
                    }
                }
            }

            SectionCard {
                Row {
                    BookCoverPreview(
                        category = category,
                        imagePath = imagePath,
                        width = 96.dp,
                        height = 126.dp,
                        iconSize = 48.sp,
                    )
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(text = "Book Cover", fontSize = 18.sp, fontWeight = FontWeight.Black)
                        Text(
                            text = if (imagePath == null) {
                                "Capture the front cover to start OCR."
                            } else {
                                "Cover captured. You can retake it if needed."
                            },
                            modifier = Modifier.padding(top = 8.dp),
                            color = InkSoft,
                            lineHeight = 22.sp,
                        )
                    }
                }
                ElevatedButton(
                    onClick = {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    enabled = !isProcessingOcr && !isSaving,
                ) {
                    Icon(Icons.Rounded.PhotoCamera, contentDescription = null)
                    Text(
                        text = if (imagePath == null) "Capture Book Cover" else "Retake Book Cover",
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }

                if (isProcessingOcr) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp),
                    )
                    Text(
                        text = "Processing OCR on the captured image...",
                        modifier = Modifier.padding(top = 10.dp),
                        color = InkSoft,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                if (ocrMessage != null) {
                    SectionCard(
                        modifier = Modifier.padding(top = 14.dp),
                        padding = androidx.compose.foundation.layout.PaddingValues(12.dp),
                    ) {
                        Text(
                            text = ocrMessage.orEmpty(),
                            color = if (ocrIsError) Danger else Leaf,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 20.sp,
                        )
                    }
                }
            }

            SectionCard {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                )
                TextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Author") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                )
                TextField(
                    value = category,
                    onValueChange = { if (it in BookCategories.all) category = it },
                    label = { Text("Category (Story / Science / History / Nature)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                )
                TextField(
                    value = pages,
                    onValueChange = { pages = it },
                    label = { Text("Pages") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                )
                TextField(
                    value = summary,
                    onValueChange = { summary = it },
                    label = { Text("Kannada summary") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    minLines = 4,
                )
                ElevatedButton(
                    onClick = {
                        scope.launch {
                            val pageCount = pages.toIntOrNull()
                            if (title.isBlank() || author.isBlank() || summary.isBlank() || pageCount == null || pageCount <= 0) {
                                snackbarHostState.showSnackbar("Please complete the title, author, pages, and summary fields.")
                                return@launch
                            }

                            isSaving = true
                            val book = onAddBook(title, author, category, pageCount, summary, imagePath)
                            isSaving = false
                            if (book == null) {
                                snackbarHostState.showSnackbar("Only teachers can add new books.")
                            } else {
                                onBookSaved(book.id)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    enabled = !isSaving && !isProcessingOcr,
                ) {
                    Text(if (isSaving) "Saving Book..." else "Add Book")
                }
            }
        }
    }
}
