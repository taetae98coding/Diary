package io.github.taetae98coding.diary.presenter.calendar.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

public class CalendarMemoFilterStateHolder(
    coroutineScope: CoroutineScope,
    strategy: CalendarMemoFilterStrategy,
) {
    public val hasFilter: StateFlow<Boolean> = strategy.hasFilter()
        .mapLatest { it.getOrDefault(false) }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )
}
