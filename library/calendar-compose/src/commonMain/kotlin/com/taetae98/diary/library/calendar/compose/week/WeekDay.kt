package com.taetae98.diary.library.calendar.compose.week

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
public fun WeekDay(
    modifier: Modifier = Modifier,
    state: WeekDayState,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(text = "state.toString()")
    }
}