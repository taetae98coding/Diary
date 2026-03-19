package io.github.taetae98coding.diary

import androidx.compose.ui.window.ComposeUIViewController
import io.github.taetae98coding.diary.app.shared.App
import platform.UIKit.UIViewController

@Suppress("FunctionName")
public fun DiaryViewController(): UIViewController {
    return ComposeUIViewController {
        App()
    }
}
