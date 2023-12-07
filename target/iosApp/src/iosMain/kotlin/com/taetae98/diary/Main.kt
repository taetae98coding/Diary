package com.taetae98.diary

import androidx.compose.ui.window.ComposeUIViewController
import com.taetae98.diary.app.App
import com.taetae98.diary.navigation.core.AppEntry
import platform.UIKit.UIViewController

public fun compose(appEntry: AppEntry): UIViewController {
    return ComposeUIViewController {
        App(entry = appEntry)
    }
}
