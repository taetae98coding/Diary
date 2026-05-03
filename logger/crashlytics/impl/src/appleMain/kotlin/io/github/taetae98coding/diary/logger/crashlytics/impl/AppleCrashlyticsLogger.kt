package io.github.taetae98coding.diary.logger.crashlytics.impl

import FirebaseCrashlytics.FIRCrashlytics
import io.github.taetae98coding.diary.logger.core.LogEntry
import io.github.taetae98coding.diary.logger.core.Logger
import io.github.taetae98coding.diary.logger.crashlytics.api.CrashlyticsLogEntry
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSError
import platform.Foundation.NSLocalizedDescriptionKey

@OptIn(ExperimentalForeignApi::class)
public data object AppleCrashlyticsLogger : Logger {
    override fun log(entry: LogEntry) {
        if (entry !is CrashlyticsLogEntry) return

        FIRCrashlytics.crashlytics().log(entry.message)
        FIRCrashlytics.crashlytics().recordError(entry.throwable.toNSError())
    }

    private fun Throwable.toNSError(): NSError {
        val userInfo = mutableMapOf<Any?, Any?>(
            "KotlinExceptionClass" to this::class.qualifiedName,
            NSLocalizedDescriptionKey to (message ?: this::class.simpleName ?: "Unknown"),
            "KotlinStackTrace" to stackTraceToString(),
        )
        cause?.let { userInfo["KotlinCause"] = it.toNSError() }

        return NSError.errorWithDomain(
            domain = this::class.qualifiedName ?: "KotlinException",
            code = 0,
            userInfo = userInfo as Map<Any?, *>,
        )
    }
}
