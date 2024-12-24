package com.example.trainymvp

import android.app.Application
import com.example.trainymvp.data.AppContainer
import com.example.trainymvp.data.AppDataContainer

class TrainyApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}