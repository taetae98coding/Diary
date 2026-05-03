package io.github.taetae98coding.diary.logger.console.impl

import io.github.aakira.napier.Napier
import io.github.taetae98coding.diary.logger.console.api.ConsoleLogEntry
import io.github.taetae98coding.diary.logger.console.api.LogLevel
import io.github.taetae98coding.diary.logger.core.LogEntry
import io.github.taetae98coding.diary.logger.core.Logger

public data object ConsoleLogger : Logger {
    override fun log(entry: LogEntry) {
        if (entry is ConsoleLogEntry) {
            when (entry.level) {
                LogLevel.VERBOSE -> Napier.v(message = entry.message, throwable = entry.throwable)
                LogLevel.DEBUG -> Napier.d(message = entry.message, throwable = entry.throwable)
                LogLevel.INFO -> Napier.i(message = entry.message, throwable = entry.throwable)
                LogLevel.WARNING -> Napier.w(message = entry.message, throwable = entry.throwable)
                LogLevel.ERROR -> Napier.e(message = entry.message, throwable = entry.throwable)
                LogLevel.ASSERT -> Napier.wtf(message = entry.message, throwable = entry.throwable)
            }
        } else {
            Napier.d(message = entry.toString())
        }
    }
}
