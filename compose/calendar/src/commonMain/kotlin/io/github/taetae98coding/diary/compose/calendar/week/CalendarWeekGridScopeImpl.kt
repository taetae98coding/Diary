package io.github.taetae98coding.diary.compose.calendar.week

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.compose.calendar.internal.WeekLocalDateRange
import io.github.taetae98coding.diary.library.datetime.overlaps
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.YearMonth

internal class CalendarWeekGridScopeImpl(
    private val yearMonth: YearMonth,
    private val weekOfMonth: Int,
) : CalendarWeekGridScope {
    private val _rows = mutableListOf(mutableListOf<CalendarWeekGridItem>())
    val rows: List<List<CalendarWeekGridItem>>
        get() = _rows.map(List<CalendarWeekGridItem>::toList)

    override fun item(
        key: Any?,
        contentType: Any?,
        localDateRange: LocalDateRange,
        content: @Composable (CalendarWeekGridItemScope.() -> Unit),
    ) {
        val weekLocalDateRange = WeekLocalDateRange(yearMonth, weekOfMonth)
        if (!(localDateRange overlaps weekLocalDateRange)) return

        val item = CalendarWeekGridItem(
            key = key,
            contentType = contentType,
            localDateRange = localDateRange,
            content = content,
        )

        _rows.last().add(item)
    }
}
