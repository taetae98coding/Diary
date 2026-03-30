package io.github.taetae98coding.diary.compose.calendar.month

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.compose.calendar.CalendarDefaults
import io.github.taetae98coding.diary.compose.calendar.CalendarSelectState
import io.github.taetae98coding.diary.compose.calendar.rememberCalendarSelectState
import io.github.taetae98coding.diary.compose.calendar.theme.CalendarColors
import io.github.taetae98coding.diary.compose.calendar.week.CalendarWeekGridScope
import io.github.taetae98coding.diary.compose.calendar.week.CalendarWeekOfMonth
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.YearMonth

@Composable
public fun CalendarMonth(
    yearMonth: YearMonth,
    modifier: Modifier = Modifier,
    selectState: CalendarSelectState = rememberCalendarSelectState(),
    primaryDayListProvider: () -> List<LocalDate> = { emptyList() },
    holidayListProvider: () -> List<LocalDateRange> = { emptyList() },
    colors: CalendarColors = CalendarDefaults.colors(),
    content: CalendarWeekGridScope.() -> Unit,
) {
    Column(modifier = modifier) {
        repeat(6) { weekOfMonth ->
            CalendarWeekOfMonth(
                yearMonth = yearMonth,
                weekOfMonth = weekOfMonth,
                selectState = selectState,
                modifier = Modifier.weight(1F),
                primaryDayListProvider = primaryDayListProvider,
                holidayListProvider = holidayListProvider,
                colors = colors,
                content = content,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    val primaryDate = LocalDate(1998, 1, 9)

    DiaryTheme {
        Surface {
            CalendarMonth(
                yearMonth = YearMonth(1998, 1),
                selectState = CalendarSelectState(),
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
