package io.github.taetae98coding.diary

import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.window.ComposeViewport
import io.github.taetae98coding.diary.app.shared.App
import kotlinx.browser.document
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.preloadFont

public fun main() {
    ComposeViewport(
        viewportContainer = requireNotNull(document.body),
    ) {
        val fontFamily = rememberNotoSansFontFamily()

        fontFamily?.let { family ->
            MaterialExpressiveTheme(typography = Typography(fontFamily = family)) {
                App()
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun rememberNotoSansFontFamily(): FontFamily? {
    val font by preloadFont(Res.font.NotoSans)
    return font?.let { FontFamily(it) }
}
