package io.github.taetae98coding.diary.app.shared.navigation3

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.RetainedValuesStoreRegistry
import androidx.compose.runtime.retain.retainRetainedValuesStoreRegistry
import androidx.navigation3.runtime.NavEntryDecorator

internal class RetainedValuesStoreNavEntryDecorator<T : Any>(registry: RetainedValuesStoreRegistry) :
    NavEntryDecorator<T>(
        onPop = { key ->
            registry.clearChild(key)
        },
        decorate = { entry ->
            registry.LocalRetainedValuesStoreProvider(entry.contentKey) { entry.Content() }
        },
    )

@Composable
internal fun <T : Any> rememberRetainedValuesStoreNavEntryDecorator(registry: RetainedValuesStoreRegistry = retainRetainedValuesStoreRegistry()): RetainedValuesStoreNavEntryDecorator<T> {
    return remember(registry) {
        RetainedValuesStoreNavEntryDecorator(registry)
    }
}
