package com.taetae98.diary.library.calendar.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.calendar.compose.month.Month

@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun Calendar(
    modifier: Modifier = Modifier,
    state: CalendarState,
) {
    HorizontalPager(
        modifier = modifier,
        state = state.pagerState,
        key = { it }
    ) {
        Month(
            modifier = Modifier.fillMaxSize(),
            state = state.getMonthState(it),
        )
    }
}
