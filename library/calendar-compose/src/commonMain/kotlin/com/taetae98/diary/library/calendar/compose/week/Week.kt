package com.taetae98.diary.library.calendar.compose.week

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.calendar.compose.model.DateRange
import kotlinx.collections.immutable.ImmutableList

@Composable
public fun Week(
    modifier: Modifier = Modifier,
    state: WeekState,
    primaryDateRange: State<ImmutableList<DateRange>>,
) {
    Column(
        modifier = modifier,
    ) {
        WeekDayLayout(
            state = state,
            primaryDateRange = primaryDateRange,
        )
    }
}

@Composable
private fun WeekDayLayout(
    modifier: Modifier = Modifier,
    state: WeekState,
    primaryDateRange: State<ImmutableList<DateRange>>,
) {
    Row(
        modifier = modifier,
    ) {
        state.weekDayState.forEach {
            WeekDay(
                modifier = Modifier.weight(1F),
                state = it,
                primaryDateRange = primaryDateRange,
            )
        }
    }
}
