package com.taetae98.diary.library.compose.calendar.week

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.compose.calendar.CalendarItem
import com.taetae98.diary.library.compose.calendar.CalendarState
import com.taetae98.diary.library.compose.calendar.model.DateRange
import kotlinx.collections.immutable.ImmutableList

@Composable
public fun Week(
    modifier: Modifier = Modifier,
    state: WeekState,
    calendarState: CalendarState,
    primaryDate: State<ImmutableList<DateRange>>,
    schedule: State<ImmutableList<CalendarItem.Schedule>>,
    holiday: State<ImmutableList<CalendarItem.Holiday>>,
) {
    Box(
        modifier = modifier,
    ) {
        WeekBackground(
            modifier = Modifier.fillMaxSize(),
            state = state,
            calendarState = calendarState,
        )
        WeekForeground(
            modifier = Modifier.fillMaxSize(),
            state = state,
            primaryDate = primaryDate,
            schedule = schedule,
            holiday = holiday,
        )
    }
}
