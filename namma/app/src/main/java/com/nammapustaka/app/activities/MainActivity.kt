package com.nammapustaka.app.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammapustaka.app.NammaPustakaApplication
import com.nammapustaka.app.ui.navigation.NammaPustakaApp
import com.nammapustaka.app.ui.theme.NammaPustakaTheme
import com.nammapustaka.app.viewmodel.LibraryViewModel
import com.nammapustaka.app.viewmodel.LibraryViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = (application as NammaPustakaApplication).container

        setContent {
            NammaPustakaTheme {
                val viewModel: LibraryViewModel = viewModel(
                    factory = LibraryViewModelFactory(appContainer),
                )
                NammaPustakaApp(viewModel = viewModel)
            }
        }
    }
}
