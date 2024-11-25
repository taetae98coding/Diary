package io.github.taetae98coding.diary.core.calendar.compose.month

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.core.calendar.compose.CalendarDefaults
import io.github.taetae98coding.diary.core.calendar.compose.color.CalendarColors
import io.github.taetae98coding.diary.core.calendar.compose.item.CalendarItemUiState
import io.github.taetae98coding.diary.core.calendar.compose.week.CalendarWeek
import io.github.taetae98coding.diary.core.calendar.compose.week.CalendarWeekState
import kotlinx.datetime.LocalDate

@Composable
internal fun CalendarMonth(
	state: CalendarMonthState,
	primaryDateListProvider: () -> List<LocalDate>,
	textItemListProvider: () -> List<CalendarItemUiState.Text>,
	holidayListProvider: () -> List<CalendarItemUiState.Holiday>,
	onCalendarItemClick: (Any) -> Unit,
	modifier: Modifier = Modifier,
	colors: CalendarColors = CalendarDefaults.colors(),
) {
	Column(modifier = modifier) {
		val weekModifier = Modifier.weight(1F)

		repeat(6) { weekOfMonth ->
			val weekState = remember {
				CalendarWeekState(
					year = state.year,
					month = state.month,
					weekOfMonth = weekOfMonth,
				)
			}

			CalendarWeek(
				state = weekState,
				primaryDateListProvider = primaryDateListProvider,
				textItemListProvider = textItemListProvider,
				holidayListProvider = holidayListProvider,
				onCalendarItemClick = onCalendarItemClick,
				modifier = weekModifier,
				colors = colors,
			)

			Drag(
				state = state,
				weekState = weekState,
			)
		}
	}
}

@Composable
private fun Drag(
	state: CalendarMonthState,
	weekState: CalendarWeekState,
) {
	val currentSelectedDateRange = state.selectedDateRange

	LaunchedEffect(currentSelectedDateRange) {
		if (currentSelectedDateRange == null) {
			weekState.finishDrag()
		} else {
			weekState.drag(currentSelectedDateRange)
		}
	}
}
