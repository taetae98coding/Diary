package io.github.taetae98coding.diary.logger.crashlytics.api

import io.github.taetae98coding.diary.logger.core.LogEntry

public data class CrashlyticsLogEntry(
    val message: String,
    val throwable: Throwable,
) : LogEntry()
