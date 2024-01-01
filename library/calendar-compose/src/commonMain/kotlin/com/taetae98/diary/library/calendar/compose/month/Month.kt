package com.taetae98.diary.library.calendar.compose.month

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.calendar.compose.week.Week

@Composable
public fun Month(
    modifier: Modifier = Modifier,
    state: MonthState,
) {
    Column(
        modifier = modifier,
    ) {
        state.weekState.forEach {
            key(it) {
                Week(
                    modifier = Modifier.fillMaxWidth()
                        .weight(1F),
                    state = it
                )
            }
        }
    }
}
