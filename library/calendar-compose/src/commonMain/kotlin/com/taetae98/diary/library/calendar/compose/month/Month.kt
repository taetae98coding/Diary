package com.taetae98.diary.library.calendar.compose.month

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.calendar.compose.CalendarItem
import com.taetae98.diary.library.calendar.compose.model.DateRange
import com.taetae98.diary.library.calendar.compose.week.Week
import kotlinx.collections.immutable.ImmutableList

@Composable
public fun Month(
    modifier: Modifier = Modifier,
    state: MonthState,
    primaryDate: State<ImmutableList<DateRange>>,
    holiday: State<ImmutableList<CalendarItem.Holiday>>,
) {
    Column(
        modifier = modifier,
    ) {
        state.weekState.forEach {
            Week(
                modifier = Modifier.fillMaxWidth()
                    .weight(1F),
                state = it,
                primaryDate = primaryDate,
                holiday = holiday,
            )
        }
    }
}
