package com.taetae98.diary.library.compose.calendar.runtime

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.taetae98.diary.library.compose.calendar.ext.CALENDAR_PAGE_SIZE
import com.taetae98.diary.library.compose.calendar.ext.toPage
import com.taetae98.diary.library.compose.calendar.CalendarState
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun rememberCalendarState(): CalendarState {
    val now = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }
    val pagerState = rememberPagerState(
        initialPage = now.date.toPage(),
        initialPageOffsetFraction = 0F
    ) {
        CALENDAR_PAGE_SIZE
    }

    return CalendarState(pagerState = pagerState)
}