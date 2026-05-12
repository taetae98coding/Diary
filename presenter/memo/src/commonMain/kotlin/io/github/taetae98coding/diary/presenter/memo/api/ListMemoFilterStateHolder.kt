@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.presenter.memo.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

public class ListMemoFilterStateHolder(
    coroutineScope: CoroutineScope,
    strategy: ListMemoFilterStrategy,
) {
    public val hasFilter: StateFlow<Boolean> = strategy.hasFilter()
        .mapLatest { it.getOrDefault(false) }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )
}
