package io.github.taetae98coding.diary.logger.crashlytics.impl

import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import io.github.taetae98coding.diary.logger.core.LogEntry
import io.github.taetae98coding.diary.logger.core.Logger
import io.github.taetae98coding.diary.logger.crashlytics.api.CrashlyticsLogEntry

public data object AndroidCrashlyticsLogger : Logger {
    override fun log(entry: LogEntry) {
        if (entry !is CrashlyticsLogEntry) return

        Firebase.crashlytics.log(entry.message)
        Firebase.crashlytics.recordException(entry.throwable)
    }
}
