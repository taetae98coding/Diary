package io.github.taetae98coding.diary

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import io.github.taetae98coding.diary.app.shared.App
import io.github.taetae98coding.diary.logger.console.impl.ConsoleLogger
import io.github.taetae98coding.diary.logger.core.DiaryLogger
import java.awt.Dimension

// iPhone 17 Pro Max 비율
private const val WIDTH = 450
private const val HEIGHT = 980

private const val MIN_WIDTH = 360
private const val MIN_HEIGHT = 784

public fun main() {
    DiaryLogger.addLogger(ConsoleLogger)

    singleWindowApplication(
        state = WindowState(size = DpSize(WIDTH.dp, HEIGHT.dp)),
        title = BuildKonfig.APP_NAME,
    ) {
        window.minimumSize = Dimension(MIN_WIDTH, MIN_HEIGHT)

        App()
    }
}
