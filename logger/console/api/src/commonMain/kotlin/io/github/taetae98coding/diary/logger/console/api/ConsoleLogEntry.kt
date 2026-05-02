package io.github.taetae98coding.diary.logger.console.api

import io.github.taetae98coding.diary.logger.core.LogEntry

public data class ConsoleLogEntry(
    val level: LogLevel,
    val message: String,
    val throwable: Throwable? = null,
) : LogEntry()
