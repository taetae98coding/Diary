package io.github.taetae98coding.diary.logger.analytics.api

import io.github.taetae98coding.diary.logger.core.LogEntry

public data class AnalyticsLogEntry(
    val name: String,
    val params: Map<String, String> = emptyMap(),
) : LogEntry()
