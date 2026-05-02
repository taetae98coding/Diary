package io.github.taetae98coding.diary.logger.analytics.impl

import FirebaseAnalytics.FIRAnalytics
import io.github.taetae98coding.diary.logger.analytics.api.AnalyticsLogEntry
import io.github.taetae98coding.diary.logger.core.LogEntry
import io.github.taetae98coding.diary.logger.core.Logger
import kotlinx.cinterop.ExperimentalForeignApi

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalForeignApi::class)
public data object AppleAnalyticsLogger : Logger {
    override fun log(entry: LogEntry) {
        if (entry !is AnalyticsLogEntry) return

        FIRAnalytics.logEventWithName(name = entry.name, parameters = entry.params as Map<Any?, *>)
    }
}
