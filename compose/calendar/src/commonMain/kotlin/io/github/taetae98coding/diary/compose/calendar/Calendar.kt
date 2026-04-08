package io.github.taetae98coding.diary.compose.calendar

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.compose.calendar.internal.toYearMonth
import io.github.taetae98coding.diary.compose.calendar.month.CalendarMonth
import io.github.taetae98coding.diary.compose.calendar.theme.CalendarColors
import io.github.taetae98coding.diary.compose.calendar.week.CalendarWeekGridScope
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.YearMonth

@Composable
public fun Calendar(
    state: CalendarState,
    modifier: Modifier = Modifier,
    primaryDayListProvider: () -> List<LocalDate> = { emptyList() },
    holidayListProvider: () -> List<LocalDateRange> = { emptyList() },
    colors: CalendarColors = CalendarDefaults.colors(),
    content: CalendarWeekGridScope.() -> Unit = {},
) {
    HorizontalPager(
        state = state.pagerState,
        modifier = modifier,
        beyondViewportPageCount = 1,
    ) { page ->
        CalendarMonth(
            yearMonth = page.toYearMonth(),
            selectState = state.selectState,
            primaryDayListProvider = primaryDayListProvider,
            holidayListProvider = holidayListProvider,
            colors = colors,
            content = content,
        )
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    val primaryDate = LocalDate(1998, 1, 9)

    DiaryTheme {
        Surface {
            Calendar(
                state = rememberCalendarState(initialYearMonth = YearMonth(1998, 1)),
                primaryDayListProvider = { listOf(primaryDate) },
                holidayListProvider = {
                    listOf(
                        LocalDate(1998, 1, 1)..LocalDate(1998, 1, 2),
                        LocalDate(1998, 1, 27)..LocalDate(1998, 1, 29),
                    )
                },
            ) {
            }
        }
    }
}
