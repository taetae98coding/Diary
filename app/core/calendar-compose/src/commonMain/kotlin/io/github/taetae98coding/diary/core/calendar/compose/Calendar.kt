package io.github.taetae98coding.diary.core.calendar.compose

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.calendar.compose.color.CalendarColors
import io.github.taetae98coding.diary.core.calendar.compose.item.CalendarItemUiState
import io.github.taetae98coding.diary.core.calendar.compose.month.CalendarMonth
import io.github.taetae98coding.diary.core.calendar.compose.month.CalendarMonthState
import io.github.taetae98coding.diary.core.calendar.compose.state.CalendarState
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@Composable
public fun Calendar(
	state: CalendarState,
	primaryDateListProvider: () -> List<LocalDate>,
	textItemListProvider: () -> List<CalendarItemUiState.Text>,
	holidayListProvider: () -> List<CalendarItemUiState.Holiday>,
	onCalendarItemClick: (Any) -> Unit,
	modifier: Modifier = Modifier,
	colors: CalendarColors = CalendarDefaults.colors(),
) {
	val coroutineScope = rememberCoroutineScope()

	Surface(
		modifier = modifier.onPreviewKeyEvent {
			when {
				!state.pagerState.isScrollInProgress && it.key == Key.DirectionRight -> {
					coroutineScope.launch { state.animateScrollToForward() }
					true
				}

				!state.pagerState.isScrollInProgress && it.key == Key.DirectionLeft -> {
					coroutineScope.launch { state.animateScrollToBackward() }
					true
				}

				else -> false
			}
		}
			.focusRequester(state.focusRequester)
			.focusable(),
		color = DiaryTheme.color.background,
	) {
		Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
			DayOfWeekRow()
			CalendarPager(
				state = state,
				primaryDateListProvider = primaryDateListProvider,
				textItemListProvider = textItemListProvider,
				holidayListProvider = holidayListProvider,
				onCalendarItemClick = onCalendarItemClick,
				colors = colors,
			)
		}
	}

	LaunchedEffect(state.focusRequester) {
		state.focusRequester.requestFocus()
	}
}

@Composable
internal fun CalendarPager(
	state: CalendarState,
	primaryDateListProvider: () -> List<LocalDate>,
	textItemListProvider: () -> List<CalendarItemUiState.Text>,
	holidayListProvider: () -> List<CalendarItemUiState.Holiday>,
	onCalendarItemClick: (Any) -> Unit,
	modifier: Modifier = Modifier,
	colors: CalendarColors = CalendarDefaults.colors(),
) {
	HorizontalPager(
		state = state.pagerState,
		modifier = modifier,
		beyondViewportPageCount = 1,
		key = { it },
	) { page ->
		val monthState = remember {
			val localDate = LocalDate(1, 1, 1).plus(page, DateTimeUnit.MONTH)

			CalendarMonthState(year = localDate.year, month = localDate.month)
		}

		CalendarMonth(
			state = monthState,
			primaryDateListProvider = primaryDateListProvider,
			textItemListProvider = textItemListProvider,
			holidayListProvider = holidayListProvider,
			onCalendarItemClick = onCalendarItemClick,
			colors = colors,
		)

		Drag(
			state = state,
			monthState = monthState,
		)
	}
}

@Composable
private fun Drag(
	state: CalendarState,
	monthState: CalendarMonthState,
) {
	val currentSelectedDateRange = state.selectedDateRange

	LaunchedEffect(currentSelectedDateRange) {
		if (currentSelectedDateRange == null) {
			monthState.finishDrag()
		} else {
			monthState.drag(currentSelectedDateRange)
		}
	}
}
