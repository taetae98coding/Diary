package io.github.taetae98coding.diary.compose.calendar

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import io.github.taetae98coding.diary.compose.calendar.internal.toPage
import io.github.taetae98coding.diary.compose.calendar.internal.toYearMonth
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearMonth

@Stable
public class CalendarState(
    internal val pagerState: PagerState,
    internal val selectState: CalendarSelectState,
) {
    public val yearMonth: YearMonth
        get() = pagerState.currentPage.toYearMonth()

    public suspend fun animateScrollToYearMonth(yearMonth: YearMonth) {
        pagerState.animateScrollToPage(yearMonth.toPage())
    }
}

@Composable
public fun rememberCalendarState(initialYearMonth: YearMonth = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()).yearMonth }): CalendarState {
    val pagerState = rememberPagerState(initialPage = initialYearMonth.toPage()) { Int.MAX_VALUE }
    val selectState = retain { CalendarSelectState() }

    return retain(
        pagerState,
        selectState,
    ) {
        CalendarState(
            pagerState = pagerState,
            selectState = selectState,
        )
    }
}
