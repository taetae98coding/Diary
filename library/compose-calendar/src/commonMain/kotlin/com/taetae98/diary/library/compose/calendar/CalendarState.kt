package com.taetae98.diary.library.compose.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.taetae98.diary.library.compose.calendar.ext.pageToLocalDate
import com.taetae98.diary.library.compose.calendar.ext.toPage
import com.taetae98.diary.library.compose.calendar.model.DateRange
import com.taetae98.diary.library.compose.calendar.month.MonthState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

@OptIn(ExperimentalFoundationApi::class)
@Stable
public class CalendarState internal constructor(
    internal val pagerState: PagerState,
) {
    private val monthStateMap = mutableMapOf<Int, MonthState>()

    internal fun getMonthState(page: Int): MonthState {
        return monthStateMap.getOrPut(page) {
            val localDate = page.pageToLocalDate()
            MonthState(localDate.year, localDate.month)
        }
    }

    public val currentYear: Int
        get() = pagerState.currentPage.pageToLocalDate().year

    public val currentMonth: Month
        get() = pagerState.currentPage.pageToLocalDate().month

    public var selectDateRange: DateRange? by mutableStateOf(null)
        internal set

    public suspend fun scrollTo(year: Int, month: Month) {
        val localDate = LocalDate(year, month, 1)
        pagerState.animateScrollToPage(localDate.toPage())
    }

    public suspend fun scrollToBackward() {
        if (pagerState.canScrollBackward) {
            pagerState.animateScrollToPage(pagerState.currentPage - 1)
        }
    }

    public suspend fun scrollToForward() {
        if (pagerState.canScrollForward) {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }
}