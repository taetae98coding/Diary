package io.github.taetae98coding.diary.logger.core

public data object DiaryLogger : Logger {
    private val loggers: MutableList<Logger> = mutableListOf()

    public fun addLogger(logger: Logger) {
        loggers += logger
    }

    override fun log(entry: LogEntry) {
        loggers.forEach { it.log(entry) }
    }
}
