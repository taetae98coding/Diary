package io.github.taetae98coding.diary.presenter.calendar.api

import io.github.taetae98coding.diary.core.model.memo.CalendarMemo
import io.github.taetae98coding.diary.library.coroutines.combine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth

public class CalendarMemoStateHolder(
    coroutineScope: CoroutineScope,
    private val strategy: CalendarMemoStrategy,
) {
    private val yearMonthFlow = MutableStateFlow<YearMonth?>(null)
    public val calendarMemo: StateFlow<List<CalendarMemo>> = yearMonthFlow.flatMapLatest { yearMonth ->
        if (yearMonth == null) {
            emptyFlow()
        } else {
            listOfNotNull(
                (yearMonth.year - 1).takeIf { yearMonth.month == Month.JANUARY },
                yearMonth.year,
                (yearMonth.year - 1).takeIf { yearMonth.month == Month.DECEMBER },
            ).map { year ->
                strategy.get(year).mapLatest { it.getOrDefault(emptyList()) }
            }.combine { it.toList().flatten() }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )

    public fun fetch(yearMonth: YearMonth) {
        yearMonthFlow.value = yearMonth
    }
}
