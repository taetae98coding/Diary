package io.github.taetae98coding.diary

import androidx.compose.ui.window.Window
import io.github.taetae98coding.diary.app.App
import platform.AppKit.NSApplication

public fun main() {
    val app = NSApplication.sharedApplication()

    Window("Diary") {
        App()
    }

    app.run()
}
