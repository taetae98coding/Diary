package io.github.taetae98coding.diary.presenter.calendar.api

import io.github.taetae98coding.diary.core.model.holiday.Holiday
import io.github.taetae98coding.diary.domain.holiday.usecase.FetchHolidayUseCase
import io.github.taetae98coding.diary.domain.holiday.usecase.GetHolidayUseCase
import io.github.taetae98coding.diary.library.coroutines.combine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth

public class CalendarHolidayStateHolder(
    private val coroutineScope: CoroutineScope,
    private val getHolidayUseCase: GetHolidayUseCase,
    private val fetchHolidayUseCase: FetchHolidayUseCase,
) {
    private val yearMonthFlow = MutableStateFlow<YearMonth?>(null)
    public val holiday: StateFlow<List<Holiday>> = yearMonthFlow.flatMapLatest { yearMonth ->
        if (yearMonth == null) {
            emptyFlow()
        } else {
            listOfNotNull(
                (yearMonth.year - 1).takeIf { yearMonth.month == Month.JANUARY },
                yearMonth.year,
                (yearMonth.year + 1).takeIf { yearMonth.month == Month.DECEMBER },
            ).map { year ->
                getHolidayUseCase(year).mapLatest { it.getOrDefault(emptyList()) }
            }.combine { it.toList().flatten() }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )

    public fun fetch(yearMonth: YearMonth) {
        yearMonthFlow.value = yearMonth

        listOfNotNull(
            (yearMonth.year - 1).takeIf { yearMonth.month == Month.JANUARY },
            yearMonth.year,
            (yearMonth.year + 1).takeIf { yearMonth.month == Month.DECEMBER },
        ).forEach { year ->
            coroutineScope.launch { fetchHolidayUseCase(year) }
        }
    }
}
