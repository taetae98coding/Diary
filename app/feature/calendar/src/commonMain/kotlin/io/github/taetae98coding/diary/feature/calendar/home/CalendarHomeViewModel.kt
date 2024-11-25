package io.github.taetae98coding.diary.feature.calendar.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.calendar.compose.item.CalendarItemUiState
import io.github.taetae98coding.diary.domain.calendar.usecase.FindCalendarMemoUseCase
import io.github.taetae98coding.diary.domain.calendar.usecase.HasCalendarFilterUseCase
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
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.koin.android.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
internal class CalendarHomeViewModel(hasCalendarFilterUseCase: HasCalendarFilterUseCase, findCalendarMemoUseCase: FindCalendarMemoUseCase) : ViewModel() {
	private val yearAndMonth = MutableStateFlow<Pair<Int, Month>?>(null)

	val hasFilter =
		hasCalendarFilterUseCase()
			.mapLatest { it.getOrNull() ?: false }
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = false,
			)

	val textItemList =
		yearAndMonth
			.filterNotNull()
			.mapLatest { (year, month) -> LocalDate(year, month, 1) }
			.mapLatest { it.minus(3, DateTimeUnit.MONTH)..it.plus(3, DateTimeUnit.MONTH) }
			.flatMapLatest { findCalendarMemoUseCase(it) }
			.mapLatest { it.getOrNull().orEmpty() }
			.mapCollectionLatest {
				CalendarItemUiState.Text(
					key = MemoKey(it.id),
					text = it.detail.title,
					color = it.detail.color,
					start = requireNotNull(it.detail.start),
					endInclusive = requireNotNull(it.detail.endInclusive),
				)
			}.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = emptyList(),
			)

	fun fetchMemo(year: Int, month: Month) {
		viewModelScope.launch {
			yearAndMonth.emit(year to month)
		}
	}
}
