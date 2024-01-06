package com.taetae98.diary.library.compose.calendar.runtime

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import com.taetae98.diary.library.compose.calendar.CalendarState
import com.taetae98.diary.library.compose.calendar.ext.CALENDAR_PAGE_SIZE
import com.taetae98.diary.library.compose.calendar.ext.toPage
import com.taetae98.diary.library.kotlin.ext.localDateNow

@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun rememberCalendarState(): CalendarState {
    val now = remember { localDateNow() }
    val pagerState = rememberPagerState(
        initialPage = now.toPage(),
        initialPageOffsetFraction = 0F
    ) {
        CALENDAR_PAGE_SIZE
    }

    return CalendarState(pagerState = pagerState)
}