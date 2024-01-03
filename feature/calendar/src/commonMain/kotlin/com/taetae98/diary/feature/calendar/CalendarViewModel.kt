package com.taetae98.diary.feature.calendar

import com.taetae98.diary.domain.usecase.holiday.GetHolidayUseCase
import com.taetae98.diary.library.calendar.compose.CalendarItem
import com.taetae98.diary.library.viewmodel.ViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.plus
import org.koin.core.annotation.Factory

@Factory
internal class CalendarViewModel(
    getHolidayUseCase: GetHolidayUseCase,
) : ViewModel() {
    private val _year = MutableStateFlow<Int?>(null)
    private val _month = MutableStateFlow<Month?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val holiday = combine(
        _year.mapNotNull { it },
        _month.mapNotNull { it },
    ) { year, month ->
        IntRange(-2, 2).map { LocalDate(year, month, 1).plus(it, DateTimeUnit.MONTH) }
    }.flatMapLatest { list ->
        combine(list.map { getHolidayUseCase(GetHolidayUseCase.Params(it.year, it.month)) }) { array ->
            array.flatMap { it.getOrNull().orEmpty() }
        }
    }.mapLatest { list ->
        list.map {
            CalendarItem.Holiday(
                name = it.name,
                start = it.start,
                endInclusive = it.endInclusive
            )
        }
    }.mapLatest {
        it.toImmutableList()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = persistentListOf()
    )

    fun setYearAndMonth(year: Int, month: Month) {
        viewModelScope.launch {
            _year.emit(year)
            _month.emit(month)
        }
    }
}