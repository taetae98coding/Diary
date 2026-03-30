package io.github.taetae98coding.diary.compose.calendar.day

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.calendar.CalendarDefaults
import io.github.taetae98coding.diary.compose.calendar.ext.toSundayBasedNumber
import io.github.taetae98coding.diary.compose.calendar.theme.CalendarColors
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.library.compose.ui.wcagAAAContentColor
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.YearMonth
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.yearMonth

@Composable
internal fun CalendarDay(
    yearMonth: YearMonth,
    weekOfMonth: Int,
    dayOfWeek: DayOfWeek,
    modifier: Modifier = Modifier,
    primaryDayListProvider: () -> List<LocalDate> = { emptyList() },
    holidayListProvider: () -> List<LocalDateRange> = { emptyList() },
    colors: CalendarColors = CalendarDefaults.colors(),
) {
    val localDate = remember(yearMonth, weekOfMonth, dayOfWeek) {
        yearMonth.firstDay
            .minus(yearMonth.firstDay.dayOfWeek.toSundayBasedNumber(), DateTimeUnit.DAY)
            .plus(weekOfMonth, DateTimeUnit.WEEK)
            .plus(dayOfWeek.toSundayBasedNumber(), DateTimeUnit.DAY)
    }

    val isPrimary by remember { derivedStateOf { localDate in primaryDayListProvider() } }
    val isHoliday by remember { derivedStateOf { holidayListProvider().any { holiday -> localDate in holiday } } }
    val isInMonth = localDate.yearMonth == yearMonth

    val color = if (isPrimary) {
        colors.primaryColor.wcagAAAContentColor()
    } else if (isInMonth) {
        when {
            localDate.dayOfWeek == DayOfWeek.SUNDAY || isHoliday -> colors.sundayColor
            localDate.dayOfWeek == DayOfWeek.SATURDAY -> colors.saturdayColor
            else -> colors.weekdayColor
        }
    } else {
        when {
            localDate.dayOfWeek == DayOfWeek.SUNDAY || isHoliday -> colors.sundayVariantColor
            localDate.dayOfWeek == DayOfWeek.SATURDAY -> colors.saturdayVariantColor
            else -> colors.weekdayVariantColor
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        PrimaryBox(
            isPrimaryProvider = { isPrimary },
            colors = colors,
        ) {
            Text(
                text = localDate.day.toString(),
                color = color,
                textAlign = TextAlign.Center,
                maxLines = 1,
                style = DiaryTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
private fun PrimaryBox(
    isPrimaryProvider: () -> Boolean,
    modifier: Modifier = Modifier,
    colors: CalendarColors = CalendarDefaults.colors(),
    content: @Composable () -> Unit,
) {
    val backgroundModifier = if (isPrimaryProvider()) {
        Modifier.background(colors.primaryColor, CircleShape)
    } else {
        Modifier
    }

    Layout(
        content = content,
        modifier = modifier
            .then(backgroundModifier)
            .padding(6.dp),
    ) { measurable, constraints ->
        val placeableList = measurable.map { it.measure(constraints) }
        val size = placeableList.maxOfOrNull { maxOf(it.measuredWidth, it.measuredHeight) } ?: 0

        layout(size, size) {
            placeableList.forEach {
                it.placeRelative(
                    x = (size - it.measuredWidth) / 2,
                    y = (size - it.measuredHeight) / 2,
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun PrimaryDayPreview() {
    val primaryDate = LocalDate(1998, 1, 9)

    DiaryTheme {
        Surface {
            CalendarDay(
                yearMonth = YearMonth(1998, 1),
                weekOfMonth = 1,
                dayOfWeek = DayOfWeek.FRIDAY,
                colors = CalendarDefaults.colors(),
                primaryDayListProvider = { listOf(primaryDate) },
            )
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    val holidayDate = LocalDate(1998, 1, 9)

    DiaryTheme {
        Surface {
            CalendarDay(
                yearMonth = YearMonth(1998, 1),
                weekOfMonth = 1,
                dayOfWeek = DayOfWeek.FRIDAY,
                colors = CalendarDefaults.colors(),
            )
        }
    }
}
