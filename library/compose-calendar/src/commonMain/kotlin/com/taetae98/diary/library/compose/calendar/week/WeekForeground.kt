package com.taetae98.diary.library.compose.calendar.week

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.compose.calendar.CalendarItem
import com.taetae98.diary.library.compose.calendar.model.DateRange
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun WeekForeground(
    modifier: Modifier = Modifier,
    state: WeekState,
    primaryDate: State<ImmutableList<DateRange>>,
    schedule: State<ImmutableList<CalendarItem.Schedule>>,
    holiday: State<ImmutableList<CalendarItem.Holiday>>,
    onItem: (key: Any) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        WeekDayLayout(
            state = state,
            primaryDate = primaryDate,
            holiday = holiday,
        )
        CalendarItemLayout(
            modifier = Modifier.fillMaxWidth()
                .weight(1F),
            state = state,
            schedule = schedule,
            holiday = holiday,
            onItem = onItem,
        )
    }
}
