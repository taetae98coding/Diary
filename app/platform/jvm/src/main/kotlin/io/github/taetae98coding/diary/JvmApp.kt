package io.github.taetae98coding.diary

import androidx.compose.ui.window.singleWindowApplication
import io.github.taetae98coding.diary.app.App

public fun main(args: Array<String>) {
    singleWindowApplication(
        title = "Diary",
    ) {
        App()
    }
}
