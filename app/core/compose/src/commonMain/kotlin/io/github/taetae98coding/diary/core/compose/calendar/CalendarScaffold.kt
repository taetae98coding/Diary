package io.github.taetae98coding.diary.core.compose.calendar

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.lifecycle.compose.LifecycleResumeEffect
import io.github.taetae98coding.diary.core.calendar.compose.Calendar
import io.github.taetae98coding.diary.core.calendar.compose.item.CalendarItemUiState
import io.github.taetae98coding.diary.core.calendar.compose.modifier.calendarDateRangeSelectable
import io.github.taetae98coding.diary.core.calendar.compose.topbar.CalendarTopBar
import io.github.taetae98coding.diary.core.calendar.compose.topbar.TodayIcon
import io.github.taetae98coding.diary.core.design.system.icon.FilterIcon
import io.github.taetae98coding.diary.core.design.system.icon.FilterOffIcon
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.library.datetime.todayIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Composable
public fun CalendarScaffold(
	state: CalendarScaffoldState,
	onSelectDate: (ClosedRange<LocalDate>) -> Unit,
	hasFilterProvider: () -> Boolean,
	textItemListProvider: () -> List<CalendarItemUiState.Text>,
	holidayListProvider: () -> List<CalendarItemUiState.Holiday>,
	onCalendarItemClick: (Any) -> Unit,
	modifier: Modifier = Modifier,
	navigationIcon: @Composable () -> Unit = {},
) {
	val coroutineScope = rememberCoroutineScope()

	Scaffold(
		modifier = modifier
			.onPreviewKeyEvent {
				when {
					!state.calendarState.isScrollInProgress && it.key == Key.DirectionRight -> {
						coroutineScope.launch { state.calendarState.animateScrollToForward() }
						true
					}

					!state.calendarState.isScrollInProgress && it.key == Key.DirectionLeft -> {
						coroutineScope.launch { state.calendarState.animateScrollToBackward() }
						true
					}

					it.key == Key.F1 -> {
						state.onFilter()
						true
					}

					!state.calendarState.isScrollInProgress && it.key == Key.F2 -> {
						coroutineScope.launch { state.calendarState.animateScrollToToday() }
						true
					}

					else -> false
				}
			}.focusRequester(state.focusRequester)
			.focusable(),
		topBar = {
			CalendarTopBar(
				state = state.calendarState,
				actions = {
					IconButton(onClick = state.onFilter) {
						Crossfade(hasFilterProvider()) { hasFilter ->
							if (hasFilter) {
								FilterIcon(tint = DiaryTheme.color.primary)
							} else {
								FilterOffIcon()
							}
						}
					}

					IconButton(onClick = { coroutineScope.launch { state.calendarState.animateScrollToToday() } }) {
						TodayIcon()
					}
				},
				navigationIcon = navigationIcon,
			)
		},
	) {
		var today by remember { mutableStateOf(LocalDate.todayIn()) }

		Calendar(
			state = state.calendarState,
			primaryDateListProvider = { listOf(today) },
			textItemListProvider = textItemListProvider,
			holidayListProvider = holidayListProvider,
			onCalendarItemClick = onCalendarItemClick,
			modifier = Modifier
				.fillMaxSize()
				.padding(it)
				.calendarDateRangeSelectable(
					state = state.calendarState,
					onSelectDate = onSelectDate,
				),
		)

		LifecycleResumeEffect(Unit) {
			today = LocalDate.todayIn()
			onPauseOrDispose { }
		}
	}

	LifecycleResumeEffect(state.focusRequester) {
		state.focusRequester.requestFocus()
		onPauseOrDispose { }
	}
}
