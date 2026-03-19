package io.github.taetae98coding.diary

import androidx.compose.ui.window.ComposeViewport
import io.github.taetae98coding.diary.app.shared.App
import kotlinx.browser.document

public fun main() {
    ComposeViewport(
        viewportContainer = requireNotNull(document.body),
    ) {
        App()
    }
}
