package com.nammapustaka.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nammapustaka.app.repository.AppContainer

class LibraryViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
            "Unknown ViewModel class: ${modelClass.name}"
        }
        return LibraryViewModel(appContainer) as T
    }
}
