package io.github.taetae98coding.diary.compose.calendar.week

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.calendar.CalendarDefaults
import io.github.taetae98coding.diary.compose.calendar.CalendarSelectState
import io.github.taetae98coding.diary.compose.calendar.day.CalendarDay
import io.github.taetae98coding.diary.compose.calendar.internal.WeekLocalDateRange
import io.github.taetae98coding.diary.compose.calendar.rememberCalendarSelectState
import io.github.taetae98coding.diary.compose.calendar.theme.CalendarColors
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.library.datetime.overlaps
import io.github.taetae98coding.diary.library.datetime.toSundayBasedDayOfWeek
import io.github.taetae98coding.diary.library.datetime.toSundayBasedNumber
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.YearMonth
import kotlinx.datetime.daysUntil

@Composable
public fun CalendarWeekOfMonth(
    yearMonth: YearMonth,
    weekOfMonth: Int,
    modifier: Modifier = Modifier,
    selectState: CalendarSelectState = rememberCalendarSelectState(),
    primaryDayListProvider: () -> List<LocalDate> = { emptyList() },
    holidayListProvider: () -> List<LocalDateRange> = { emptyList() },
    colors: CalendarColors = CalendarDefaults.colors(),
    content: CalendarWeekGridScope.() -> Unit = {},
) {
    val weekLocalDateRange = remember { WeekLocalDateRange(yearMonth, weekOfMonth) }

    Column(
        modifier = modifier.drawBehind {
            val selectedLocalDateRange = selectState.localDateRange ?: return@drawBehind
            val drawLocalDateRange = maxOf(selectedLocalDateRange.start, weekLocalDateRange.start)..minOf(selectedLocalDateRange.endInclusive, weekLocalDateRange.endInclusive)

            if (weekLocalDateRange overlaps drawLocalDateRange) {
                drawRoundRect(
                    color = colors.selectedColor,
                    topLeft = Offset(size.width / 7F * drawLocalDateRange.start.dayOfWeek.toSundayBasedNumber(), 0F),
                    size = size.copy(width = size.width / 7F * (drawLocalDateRange.start.daysUntil(drawLocalDateRange.endInclusive) + 1)),
                )
            }
        },
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        HorizontalDivider(thickness = 0.5.dp)
        CalendarDayRow(
            yearMonth = yearMonth,
            weekOfMonth = weekOfMonth,
            primaryDayListProvider = primaryDayListProvider,
            holidayListProvider = holidayListProvider,
            colors = colors,
        )
        CalendarWeekGrid(
            yearMonth = yearMonth,
            weekOfMonth = weekOfMonth,
            modifier = Modifier.weight(1F),
            content = content,
        )
    }
}

@Composable
private fun CalendarDayRow(
    yearMonth: YearMonth,
    weekOfMonth: Int,
    modifier: Modifier = Modifier,
    primaryDayListProvider: () -> List<LocalDate> = { emptyList() },
    holidayListProvider: () -> List<LocalDateRange> = { emptyList() },
    colors: CalendarColors = CalendarDefaults.colors(),
) {
    Row(modifier = modifier) {
        repeat(7) { dayOfWeek ->
            CalendarDay(
                yearMonth = yearMonth,
                weekOfMonth = weekOfMonth,
                dayOfWeek = dayOfWeek.toSundayBasedDayOfWeek(),
                modifier = Modifier.weight(1F),
                primaryDayListProvider = primaryDayListProvider,
                holidayListProvider = holidayListProvider,
                colors = colors,
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
            CalendarWeekOfMonth(
                yearMonth = YearMonth(1998, 1),
                weekOfMonth = 1,
                selectState = CalendarSelectState(),
                primaryDayListProvider = { listOf(primaryDate) },
                holidayListProvider = { listOf(LocalDate(1998, 1, 5)..LocalDate(1998, 1, 5)) },
            ) {
            }
        }
    }
}
