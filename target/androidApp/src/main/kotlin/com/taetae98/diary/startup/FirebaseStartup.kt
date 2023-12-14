package com.taetae98.diary.startup

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.FirebaseApp

internal class FirebaseStartup : Initializer<Unit> {
    override fun create(context: Context) {
        FirebaseApp.initializeApp(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}