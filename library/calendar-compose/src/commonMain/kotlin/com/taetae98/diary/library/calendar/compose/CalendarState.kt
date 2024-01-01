package com.taetae98.diary.library.calendar.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Stable
import com.taetae98.diary.library.calendar.compose.ext.pageToLocalDate
import com.taetae98.diary.library.calendar.compose.month.MonthState
import kotlinx.datetime.Month

@OptIn(ExperimentalFoundationApi::class)
@Stable
public class CalendarState internal constructor(
    internal val pagerState: PagerState,
) {
    private val monthStateMap = mutableMapOf<Int, MonthState>()
    public val currentYear: Int
        get() = pagerState.currentPage.pageToLocalDate().year

    public val currentMonth: Month
        get() = pagerState.currentPage.pageToLocalDate().month

    internal fun getMonthState(page: Int): MonthState {
        return monthStateMap.getOrPut(page) {
            val localDate = page.pageToLocalDate()
            MonthState(localDate.year, localDate.month)
        }
    }
}