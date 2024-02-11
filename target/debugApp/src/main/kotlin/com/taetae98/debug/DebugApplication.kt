package com.taetae98.debug

import android.app.Application
import com.google.firebase.FirebaseApp

internal class DebugApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
