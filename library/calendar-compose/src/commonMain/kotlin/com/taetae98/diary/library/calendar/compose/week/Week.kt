package com.taetae98.diary.library.calendar.compose.week

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier

@Composable
public fun Week(
    modifier: Modifier = Modifier,
    state: WeekState
) {
    Row(
        modifier = modifier,
    ) {
        state.weekDayState.forEach {
            key(it) {
                WeekDay(
                    modifier = Modifier.weight(1F)
                        .fillMaxHeight(),
                    state = it,
                )
            }
        }
    }
}
