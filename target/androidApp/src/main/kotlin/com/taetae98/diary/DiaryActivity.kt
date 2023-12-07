package com.taetae98.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.taetae98.diary.app.App
import com.taetae98.diary.navigation.core.AppEntry

internal class DiaryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = defaultComponentContext()
        val appEntry = AppEntry(context = context)

        setContent {
            App(entry = appEntry)
        }
    }
}