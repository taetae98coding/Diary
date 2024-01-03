package com.taetae98.diary.feature.calendar

import com.taetae98.diary.domain.usecase.holiday.GetHolidayUseCase
import com.taetae98.diary.library.viewmodel.ViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Month
import org.koin.core.annotation.Factory

@Factory
internal class CalendarViewModel(
    getHolidayUseCase: GetHolidayUseCase,
) : ViewModel() {
    val holiday = getHolidayUseCase(GetHolidayUseCase.Params(2024, Month.JANUARY))
        .mapLatest { it.getOrNull().orEmpty() }
        .onEach {
            println("result : $it")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
}