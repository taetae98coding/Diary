package com.taetae98.diary.feature.calendar

import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.holiday.GetHolidayUseCase
import com.taetae98.diary.domain.usecase.memo.FindMemoByDateRangeUseCase
import com.taetae98.diary.library.compose.calendar.CalendarItem
import com.taetae98.diary.library.kotlin.ext.toChristDayNumber
import com.taetae98.diary.library.viewmodel.ViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
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
    findMemoByDateRangeUseCase: FindMemoByDateRangeUseCase,
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val ownerId = getAccountUseCase(Unit).mapLatest { it.getOrNull() }
        .mapLatest { it?.uid }
    private val _year = MutableStateFlow<Int?>(null)
    private val _month = MutableStateFlow<Month?>(null)

    private val yearAndMonth = combine(
        _year.filterNotNull(),
        _month.filterNotNull(),
    ) { year, month ->
        year to month
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val schedule = combine(ownerId, yearAndMonth) { ownerId, yearAndMonth ->
        val start = LocalDate(yearAndMonth.first, yearAndMonth.second, 1)
            .minus(1, DateTimeUnit.MONTH)
            .run { minus(dayOfWeek.toChristDayNumber(), DateTimeUnit.DAY) }
        val endInclusive = LocalDate(yearAndMonth.first, yearAndMonth.second, 1)
            .plus(1, DateTimeUnit.MONTH)
            .run { minus(dayOfWeek.toChristDayNumber(), DateTimeUnit.DAY) }
            .run { plus(6, DateTimeUnit.WEEK) }
            .run { minus(1, DateTimeUnit.DAY) }

        findMemoByDateRangeUseCase(FindMemoByDateRangeUseCase.Params(ownerId, start..endInclusive))
    }.flatMapLatest {
        it
    }.mapLatest {
        it.getOrNull().orEmpty()
    }.mapLatest { list ->
        list.mapNotNull {
            CalendarItem.Schedule(
                key = MemoCalendarItemKey(it.id),
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
    val holiday = yearAndMonth.flatMapLatest { yearAndMonth ->
        val list = IntRange(-2, 2).map {
            LocalDate(yearAndMonth.first, yearAndMonth.second, 1).plus(it, DateTimeUnit.MONTH)
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