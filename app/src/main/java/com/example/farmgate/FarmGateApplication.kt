package com.example.farmgate


import android.app.Application
import com.example.farmgate.core.common.AppContainer

class FarmGateApplication : Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}