package io.github.taetae98coding.diary.feature.buddy.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.github.taetae98coding.diary.core.calendar.compose.item.CalendarItemUiState
import io.github.taetae98coding.diary.core.navigation.buddy.BuddyGroupCalendarDestination
import io.github.taetae98coding.diary.domain.buddy.usecase.PageBuddyGroupCalendarMemo
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
internal class BuddyGroupCalendarMemoViewModel(
	savedStateHandle: SavedStateHandle,
	private val pageBuddyGroupCalendarMemo: PageBuddyGroupCalendarMemo,
) : ViewModel() {
	private val route = savedStateHandle.toRoute<BuddyGroupCalendarDestination>()
	private val yearAndMonth = MutableStateFlow<Pair<Int, Month>?>(null)

	private val refreshCount = MutableStateFlow(0)

	val memoList = yearAndMonth
		.filterNotNull()
		.mapLatest { (year, month) -> LocalDate(year, month, 1) }
		.mapLatest { it.minus(3, DateTimeUnit.MONTH)..it.plus(3, DateTimeUnit.MONTH) }
		.flatMapLatest { dateRange ->
			refreshCount.flatMapLatest {
				pageBuddyGroupCalendarMemo(route.groupId, dateRange)
			}
		}.mapLatest { it.getOrNull() }
		.filterNotNull()
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

	fun refresh() {
		refreshCount.value++
	}
}
