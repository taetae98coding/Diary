package io.github.taetae98coding.diary.initializer

import android.content.Context
import androidx.startup.Initializer
import io.github.taetae98coding.diary.logger.analytics.impl.AndroidAnalyticsLogger
import io.github.taetae98coding.diary.logger.console.impl.ConsoleLogger
import io.github.taetae98coding.diary.logger.core.DiaryLogger
import io.github.taetae98coding.diary.logger.crashlytics.impl.AndroidCrashlyticsLogger

internal class LoggerInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        DiaryLogger.addLogger(ConsoleLogger)
        DiaryLogger.addLogger(AndroidAnalyticsLogger)
        DiaryLogger.addLogger(AndroidCrashlyticsLogger)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
