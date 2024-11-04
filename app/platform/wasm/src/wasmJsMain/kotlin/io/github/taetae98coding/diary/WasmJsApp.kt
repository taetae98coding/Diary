package io.github.taetae98coding.diary

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import io.github.taetae98coding.diary.app.App
import io.github.taetae98coding.diary.core.coroutines.AppLifecycleOwner
import io.github.taetae98coding.diary.initializer.intiJs

@OptIn(ExperimentalComposeUiApi::class)
public fun main() {
    intiJs()

    CanvasBasedWindow(
        title = "Diary",
        canvasElementId = "compose",
    ) {
        App()

        LifecycleStartEffect(keys = arrayOf(AppLifecycleOwner)) {
            AppLifecycleOwner.start()
            onStopOrDispose { AppLifecycleOwner.stop() }
        }

        LifecycleResumeEffect(keys = arrayOf(AppLifecycleOwner)) {
            AppLifecycleOwner.resume()
            onPauseOrDispose { AppLifecycleOwner.pause() }
        }
    }
}
