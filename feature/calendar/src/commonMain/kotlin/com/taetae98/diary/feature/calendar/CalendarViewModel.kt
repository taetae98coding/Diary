package com.taetae98.diary.feature.calendar

import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.holiday.GetHolidayUseCase
import com.taetae98.diary.domain.usecase.memo.FindByDateRangeUseCase
import com.taetae98.diary.library.compose.calendar.CalendarItem
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
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.koin.core.annotation.Factory

@Factory
internal class CalendarViewModel(
    getHolidayUseCase: GetHolidayUseCase,
    getAccountUseCase: GetAccountUseCase,
    findByDateRangeUseCase: FindByDateRangeUseCase,
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val ownerId = getAccountUseCase(Unit).mapLatest { it.getOrNull() }
        .mapLatest { it?.uid }
    private val _year = MutableStateFlow<Int?>(null)
    private val _month = MutableStateFlow<Month?>(null)

    private val yearAndMonth = combine(
        _year.mapNotNull { it },
        _month.mapNotNull { it },
    ) { year, month ->
        LocalDate(year, month, 1).minus(2, DateTimeUnit.MONTH)..LocalDate(year, month, 1).plus(2, DateTimeUnit.MONTH)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val schedule = combine(ownerId, yearAndMonth) { ownerId, range ->
        findByDateRangeUseCase(FindByDateRangeUseCase.Params(range, ownerId))
    }.flatMapLatest {
        it
    }.mapLatest {
        it.getOrNull().orEmpty()
    }.mapLatest { list ->
        list.mapNotNull {
            CalendarItem.Schedule(
                key = it.id,
                name = it.title,
                color = it.dateRangeColor ?: return@mapNotNull null,
                start = it.dateRange?.start ?: return@mapNotNull null,
                endInclusive = it.dateRange?.endInclusive ?: return@mapNotNull null,
            )
        }
    }.mapLatest {
        it.toImmutableList()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = persistentListOf()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val holiday = yearAndMonth.flatMapLatest { range ->
        val list = buildList {
            var cursor = range.start
            while (cursor <= range.endInclusive) {
                add(cursor)
                cursor = cursor.plus(1, DateTimeUnit.MONTH)
            }
        }

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