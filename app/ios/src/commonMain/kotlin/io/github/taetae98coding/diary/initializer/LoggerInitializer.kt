package io.github.taetae98coding.diary.initializer

import io.github.taetae98coding.diary.logger.analytics.impl.AppleAnalyticsLogger
import io.github.taetae98coding.diary.logger.console.impl.ConsoleLogger
import io.github.taetae98coding.diary.logger.core.DiaryLogger
import io.github.taetae98coding.diary.logger.crashlytics.impl.AppleCrashlyticsLogger

public fun setupLogger() {
    DiaryLogger.addLogger(ConsoleLogger)
    DiaryLogger.addLogger(AppleAnalyticsLogger)
    DiaryLogger.addLogger(AppleCrashlyticsLogger)
}
