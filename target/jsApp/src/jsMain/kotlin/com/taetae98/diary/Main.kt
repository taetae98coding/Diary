package com.taetae98.diary

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.taetae98.diary.app.App
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
public fun main() {
    onWasmReady {
        CanvasBasedWindow {
            App()
        }
    }
}