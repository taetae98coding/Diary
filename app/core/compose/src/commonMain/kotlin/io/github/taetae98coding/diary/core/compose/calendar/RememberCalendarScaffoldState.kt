package io.github.taetae98coding.diary.core.compose.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.taetae98coding.diary.core.calendar.compose.state.rememberCalendarState

@Composable
public fun rememberCalendarScaffoldState(
	onFilter: () -> Unit,
): CalendarScaffoldState {
	val calendarState = rememberCalendarState()

	return remember {
		CalendarScaffoldState(
			onFilter = onFilter,
			calendarState = calendarState,
		)
	}
}
