package com.house.playtest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PlayTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
