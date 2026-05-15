package com.nammapustaka.app.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.nammapustaka.app.ui.components.SectionCard
import com.nammapustaka.app.ui.theme.GoldLight
import com.nammapustaka.app.ui.theme.InkMuted
import com.nammapustaka.app.ui.theme.InkSoft
import com.nammapustaka.app.ui.theme.SaffronLight
import com.nammapustaka.app.utils.BarcodeQrAnalyzer
import com.nammapustaka.app.viewmodel.AppUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScanScreen(
    uiState: AppUiState,
    onBorrowFromQr: suspend (String) -> String?,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var manualCode by rememberSaveable { mutableStateOf("") }
    var handlingScan by rememberSaveable { mutableStateOf(false) }
    var hasCameraPermission by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        hasCameraPermission = granted
    }

    fun processCode(code: String) {
        if (handlingScan) return
        scope.launch {
            handlingScan = true
            val message = onBorrowFromQr(code)
            snackbarHostState.showSnackbar(message ?: "Book issued successfully.")
            delay(2_000)
            handlingScan = false
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("QR Scan") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SectionCard {
                Text(
                    text = if (uiState.isStudent) {
                        "Scan a book QR code to issue the book for 14 days."
                    } else {
                        "Teacher mode is view-only here. Borrowing works only for logged-in students."
                    },
                    color = InkMuted,
                )
            }

            if (!hasCameraPermission) {
                SectionCard {
                    Text(
                        text = "Camera access is needed for live QR scanning.",
                        color = InkSoft,
                        fontSize = 14.sp,
                    )
                    ElevatedButton(
                        onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                    ) {
                        Text("Enable Camera")
                    }
                }
            } else {
                SectionCard(
                    padding = androidx.compose.foundation.layout.PaddingValues(12.dp),
                ) {
                    QrScannerPreview(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp),
                        onCodeScanned = { code ->
                            if (!handlingScan) processCode(code)
                        },
                    )
                    Text(
                        text = "Point the camera at a book QR code",
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }
            }

            SectionCard {
                Text(text = "Manual code entry", fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Black)
                Text(
                    text = "If the camera is unavailable, enter the printed QR text such as `BOOK_1700000000000001`. Legacy `book:3` codes still work.",
                    modifier = Modifier.padding(top = 8.dp),
                    color = InkSoft,
                )
                TextField(
                    value = manualCode,
                    onValueChange = { manualCode = it },
                    label = { Text("Book code") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                )
                ElevatedButton(
                    onClick = { processCode(manualCode) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                ) {
                    Text("Issue Using Code")
                }
            }
        }
    }
}

@Composable
private fun QrScannerPreview(
    modifier: Modifier = Modifier,
    onCodeScanned: (String) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val analyzer = remember { BarcodeQrAnalyzer(onCodeScanned) }
    val executor = remember { Executors.newSingleThreadExecutor() }

    DisposableEffect(lifecycleOwner) {
        val listener = Runnable {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().apply {
                surfaceProvider = previewView.surfaceProvider
            }
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .apply {
                    setAnalyzer(executor, analyzer)
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis,
                )
            } catch (_: Exception) {
            }
        }
        cameraProviderFuture.addListener(listener, ContextCompat.getMainExecutor(context))

        onDispose {
            runCatching { cameraProviderFuture.get().unbindAll() }
            analyzer.close()
            executor.shutdown()
        }
    }

    AndroidView(
        factory = { previewView },
        modifier = modifier,
    )
}
