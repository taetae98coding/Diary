package io.github.taetae98coding.diary

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.github.taetae98coding.diary.app.App

@OptIn(ExperimentalComposeUiApi::class)
public fun main() {
    CanvasBasedWindow(
        title = "Diary",
        canvasElementId = "compose",
    ) {
        App()
    }
}
