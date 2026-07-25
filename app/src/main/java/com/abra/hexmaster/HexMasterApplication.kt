package com.abra.hexmaster

import android.app.Application
import com.abra.hexmaster.di.AppContainer

class HexMasterApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
