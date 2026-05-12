@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.feature.calendar.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.model.holiday.Holiday
import io.github.taetae98coding.diary.domain.holiday.usecase.FetchHolidayUseCase
import io.github.taetae98coding.diary.domain.holiday.usecase.GetHolidayUseCase
import io.github.taetae98coding.diary.library.coroutines.combine
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class CalendarHolidayViewModel(
    private val getHolidayUseCase: GetHolidayUseCase,
    private val fetchHolidayUseCase: FetchHolidayUseCase,
) : ViewModel() {
    private val yearMonthFlow = MutableStateFlow<YearMonth?>(null)

    val holiday: StateFlow<List<Holiday>> = yearMonthFlow.flatMapLatest { yearMonth ->
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
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )

    fun fetch(yearMonth: YearMonth) {
        yearMonthFlow.value = yearMonth

        listOfNotNull(
            (yearMonth.year - 1).takeIf { yearMonth.month == Month.JANUARY },
            yearMonth.year,
            (yearMonth.year + 1).takeIf { yearMonth.month == Month.DECEMBER },
        ).forEach { year ->
            viewModelScope.launch { fetchHolidayUseCase(year) }
        }
    }
}
