package io.github.taetae98coding.diary.feature.calendar.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.taetae98coding.diary.core.calendar.compose.state.rememberCalendarState

@Composable
internal fun rememberCalendarHomeScreenState(): CalendarHomeScreenState {
	val calendarState = rememberCalendarState()

	return remember {
		CalendarHomeScreenState(
			calendarState = calendarState,
		)
	}
}
