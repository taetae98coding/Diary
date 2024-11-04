package io.github.taetae98coding.diary

import androidx.compose.ui.window.singleWindowApplication
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import io.github.taetae98coding.diary.app.App
import io.github.taetae98coding.diary.core.coroutines.AppLifecycleOwner
import io.github.taetae98coding.diary.initializer.intiJvm
import kotlinx.coroutines.runBlocking

public fun main() {
    runBlocking {
        intiJvm()
    }

    singleWindowApplication(
        title = "Diary",
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

    destroyJvm()
}

private fun destroyJvm() {
    AppLifecycleOwner.destroy()
}
