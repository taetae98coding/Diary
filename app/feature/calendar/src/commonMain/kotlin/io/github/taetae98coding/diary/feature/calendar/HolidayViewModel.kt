package io.github.taetae98coding.diary.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.calendar.compose.item.CalendarItemUiState
import io.github.taetae98coding.diary.domain.holiday.usecase.FindHolidayUseCase
import io.github.taetae98coding.diary.library.coroutines.combine
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.plus
import org.koin.android.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
internal class HolidayViewModel(
    findHolidayUseCase: FindHolidayUseCase,
) : ViewModel() {
    private val yearAndMonth = MutableStateFlow<Pair<Int, Month>?>(null)

    val holidayList = yearAndMonth.filterNotNull()
        .mapLatest { (year, month) -> LocalDate(year, month, 1) }
        .mapLatest { localDate -> IntRange(-2, 2).map { localDate.plus(it, DateTimeUnit.MONTH) } }
        .mapLatest { list -> list.map { findHolidayUseCase(it.year, it.month) } }
        .flatMapLatest { list -> list.combine { array -> array.flatMap { it.getOrNull().orEmpty() } } }
        .mapCollectionLatest {
            CalendarItemUiState.Holiday(
                text = it.name,
                start = it.start,
                endInclusive = it.endInclusive,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    fun fetchHoliday(year: Int, month: Month) {
        viewModelScope.launch {
            yearAndMonth.emit(year to month)
        }
    }
}
