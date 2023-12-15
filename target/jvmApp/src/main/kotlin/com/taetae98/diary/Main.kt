package com.taetae98.diary


import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.taetae98.diary.app.App
import com.taetae98.diary.app.AppModule
import com.taetae98.diary.navigation.core.app.AppEntry
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

public fun main() {
    val lifecycle = LifecycleRegistry()
    val context = DefaultComponentContext(lifecycle = lifecycle)
    val appEntry = AppEntry(context = context)

    startKoin {
        modules(AppModule().module)
    }

    singleWindowApplication {
        App(entry = appEntry)
    }
}