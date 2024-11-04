package io.github.taetae98coding.diary.feature.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.taetae98coding.diary.core.calendar.compose.state.rememberCalendarState

@Composable
internal fun rememberCalendarScreenState(): CalendarScreenState {
    val calendarState = rememberCalendarState()

    return remember {
        CalendarScreenState(
            calendarState = calendarState,
        )
    }
}
