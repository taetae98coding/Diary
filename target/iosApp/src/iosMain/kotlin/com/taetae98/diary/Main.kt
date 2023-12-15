package com.taetae98.diary

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.taetae98.diary.app.App
import com.taetae98.diary.navigation.core.app.AppEntry
import platform.UIKit.UIViewController

public fun compose(): UIViewController {
    return ComposeUIViewController {
        App(entry = remember { AppEntry(DefaultComponentContext(LifecycleRegistry())) })
    }
}
