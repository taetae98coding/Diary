package com.taetae98.diary.library.calendar.compose.week

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public fun Week(
    modifier: Modifier = Modifier,
    state: WeekState
) {
    Column(
        modifier = modifier,
    ) {
        WeekDayLayout(state = state)
    }
}

@Composable
private fun WeekDayLayout(
    modifier: Modifier = Modifier,
    state: WeekState
) {
    Row(
        modifier = modifier,
    ) {
        state.weekDayState.forEach {
            WeekDay(
                modifier = Modifier.weight(1F),
                state = it,
            )
        }
    }
}
