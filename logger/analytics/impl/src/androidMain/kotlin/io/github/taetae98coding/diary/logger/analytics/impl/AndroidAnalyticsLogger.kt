package io.github.taetae98coding.diary.logger.analytics.impl

import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import io.github.taetae98coding.diary.logger.analytics.api.AnalyticsLogEntry
import io.github.taetae98coding.diary.logger.core.LogEntry
import io.github.taetae98coding.diary.logger.core.Logger

public data object AndroidAnalyticsLogger : Logger {
    override fun log(entry: LogEntry) {
        if (entry !is AnalyticsLogEntry) return

        val bundle = Bundle()
            .apply { entry.params.forEach { (key, value) -> putString(key, value) } }

        Firebase.analytics.logEvent(entry.name, bundle)
    }
}
