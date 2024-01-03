package com.taetae98.diary.library.compose.calendar.week

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.compose.calendar.CalendarItem
import com.taetae98.diary.library.compose.calendar.model.DateRange
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun WeekDayLayout(
    modifier: Modifier = Modifier,
    state: WeekState,
    primaryDate: State<ImmutableList<DateRange>>,
    holiday: State<ImmutableList<CalendarItem.Holiday>>,
) {
    Row(
        modifier = modifier,
    ) {
        state.weekDayState.forEach {
            WeekDay(
                modifier = Modifier.weight(1F),
                state = it,
                primaryDate = primaryDate,
                holiday = holiday,
            )
        }
    }
}