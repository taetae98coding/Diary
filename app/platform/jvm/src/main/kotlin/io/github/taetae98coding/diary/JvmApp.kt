package io.github.taetae98coding.diary

import androidx.compose.ui.window.singleWindowApplication
import io.github.taetae98coding.diary.app.App

public fun main() {
    singleWindowApplication(
        title = "Diary",
    ) {
        App()
    }
}
