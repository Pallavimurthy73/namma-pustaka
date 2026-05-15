package com.nammapustaka.app.repository

import android.content.Context
import com.nammapustaka.app.database.NammaPustakaDatabase

class AppContainer(context: Context) {
    private val database = NammaPustakaDatabase.getInstance(context)

    val authRepository = AuthRepository(database.userDao())
    val libraryRepository = LibraryRepository(database)
}
