package com.nammapustaka.app

import android.app.Application
import com.nammapustaka.app.repository.AppContainer

class NammaPustakaApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
