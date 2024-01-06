package com.taetae98.diary.library.compose.calendar.month

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.compose.calendar.CalendarItem
import com.taetae98.diary.library.compose.calendar.CalendarState
import com.taetae98.diary.library.compose.calendar.model.DateRange
import com.taetae98.diary.library.compose.calendar.week.Week
import kotlinx.collections.immutable.ImmutableList

@Composable
public fun Month(
    modifier: Modifier = Modifier,
    state: MonthState,
    calendarState: CalendarState,
    primaryDate: State<ImmutableList<DateRange>>,
    schedule: State<ImmutableList<CalendarItem.Schedule>>,
    holiday: State<ImmutableList<CalendarItem.Holiday>>,
    onHoliday: (key: Any) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        state.weekState.forEach {
            Week(
                modifier = Modifier.fillMaxWidth()
                    .weight(1F),
                state = it,
                calendarState = calendarState,
                primaryDate = primaryDate,
                schedule = schedule,
                holiday = holiday,
                onHoliday = onHoliday,
            )
        }
    }
}
