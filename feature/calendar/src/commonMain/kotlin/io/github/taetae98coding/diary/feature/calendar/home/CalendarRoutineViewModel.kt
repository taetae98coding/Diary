@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.feature.calendar.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.model.routine.CalendarRoutine
import io.github.taetae98coding.diary.domain.routine.usecase.GetCalendarRoutineUseCase
import io.github.taetae98coding.diary.library.coroutines.combine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class CalendarRoutineViewModel(private val getCalendarRoutineUseCase: GetCalendarRoutineUseCase) : ViewModel() {
    private val yearMonthFlow = MutableStateFlow<YearMonth?>(null)

    val calendarRoutine: StateFlow<List<CalendarRoutine>> = yearMonthFlow.flatMapLatest { yearMonth ->
        if (yearMonth == null) {
            emptyFlow()
        } else {
            listOfNotNull(
                (yearMonth.year - 1).takeIf { yearMonth.month == Month.JANUARY },
                yearMonth.year,
                (yearMonth.year + 1).takeIf { yearMonth.month == Month.DECEMBER },
            ).map { year ->
                getCalendarRoutineUseCase(year).mapLatest { it.getOrDefault(emptyList()) }
            }.combine { it.toList().flatten() }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )

    fun fetchYearMonth(yearMonth: YearMonth) {
        yearMonthFlow.value = yearMonth
    }
}
