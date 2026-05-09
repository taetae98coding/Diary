package io.github.taetae98coding.diary.app.shared.navigation3

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation3.runtime.NavEntryDecorator
import io.github.taetae98coding.diary.logger.analytics.api.AnalyticsLogEntry
import io.github.taetae98coding.diary.logger.core.DiaryLogger

internal class AnalyticsNavEntryDecorator<T : Any> :
    NavEntryDecorator<T>(
        decorate = { entry ->
            val screenName = entry.contentKey::class.simpleName.orEmpty()

            LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
                DiaryLogger.log(
                    AnalyticsLogEntry(
                        name = "screen_view",
                        params = mapOf("screen_name" to screenName),
                    ),
                )
            }

            entry.Content()
        },
    )

@Composable
internal fun <T : Any> rememberAnalyticsNavEntryDecorator(): AnalyticsNavEntryDecorator<T> {
    return remember { AnalyticsNavEntryDecorator() }
}
