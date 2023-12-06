package com.taetae98.diary

import androidx.compose.material3.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
public fun main() {
    onWasmReady {
        CanvasBasedWindow {
            Text(text = "Hello World")
        }
    }
}