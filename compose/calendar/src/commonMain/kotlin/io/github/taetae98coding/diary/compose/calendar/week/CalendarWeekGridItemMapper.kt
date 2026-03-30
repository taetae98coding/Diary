package io.github.taetae98coding.diary.compose.calendar.week

import androidx.compose.foundation.lazy.grid.GridItemSpan
import io.github.taetae98coding.diary.compose.calendar.internal.WeekLocalDateRange
import io.github.taetae98coding.diary.compose.calendar.ext.toSundayBasedNumber
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.YearMonth

internal class CalendarWeekGridItemMapper(
    private val yearMonth: YearMonth,
    private val weekOfMonth: Int,
) {
    private val weekRange: LocalDateRange
        get() = WeekLocalDateRange(yearMonth, weekOfMonth)

    private fun buildSpace(span: Int): CalendarWeekLazyGridItem {
        return CalendarWeekLazyGridItem(
            key = null,
            span = { GridItemSpan(span) },
            contentType = SPACE_CONTENT_TYPE,
            content = {},
        )
    }

    private fun buildItem(item: CalendarWeekGridItem): CalendarWeekLazyGridItem {
        return CalendarWeekLazyGridItem(
            key = item.key,
            span = { GridItemSpan(item.localDateRange.endInclusive.dayOfWeek.toSundayBasedNumber() - item.localDateRange.start.dayOfWeek.toSundayBasedNumber() + 1) },
            contentType = item.contentType,
            content = item.content,
        )
    }

    private fun buildPlaceable(items: List<CalendarWeekGridItem>): List<Placeable> {
        val isCheckedGrid = Array(items.size) { BooleanArray(7) { false } }

        return items.map { item ->
            val item = item.copy(
                localDateRange = LocalDateRange(
                    maxOf(item.localDateRange.start, weekRange.start),
                    minOf(item.localDateRange.endInclusive, weekRange.endInclusive),
                ),
            )

            val itemColumnIndexList = item.localDateRange.map { it.dayOfWeek.toSundayBasedNumber() }
            val row = isCheckedGrid.indexOfFirst { row -> itemColumnIndexList.none { row[it] } }

            itemColumnIndexList.forEach { isCheckedGrid[row][it] = true }
            Placeable(row, item)
        }
    }

    fun mapToCalendarWeekLazyGridItem(items: List<CalendarWeekGridItem>): List<CalendarWeekLazyGridItem> {
        var currentRow = 0
        var currentChristDayNumber = DayOfWeek.SUNDAY.toSundayBasedNumber()

        return buildList {
            fun addEndPaddingIfNeed() {
                if (currentChristDayNumber <= DayOfWeek.SATURDAY.toSundayBasedNumber()) {
                    add(buildSpace(DayOfWeek.SATURDAY.toSundayBasedNumber() - currentChristDayNumber + 1))
                }
            }

            buildPlaceable(items).sortedWith(compareBy(Placeable::row, { it.item.localDateRange.start }))
                .forEach { placeable ->
                    if (placeable.row != currentRow) {
                        addEndPaddingIfNeed()
                        currentRow = placeable.row
                        currentChristDayNumber = DayOfWeek.SUNDAY.toSundayBasedNumber()
                    }

                    val startPadding = placeable.item.localDateRange.start.dayOfWeek.toSundayBasedNumber() - currentChristDayNumber
                    if (startPadding > 0) {
                        add(buildSpace(startPadding))
                    }

                    add(buildItem(placeable.item))
                    currentChristDayNumber = placeable.item.localDateRange.endInclusive.dayOfWeek.toSundayBasedNumber() + 1
                }

            addEndPaddingIfNeed()
        }
    }

    private data class Placeable(
        val row: Int,
        val item: CalendarWeekGridItem,
    )

    companion object {
        const val SPACE_CONTENT_TYPE = "CalendarWeekGridUtil.Space"
    }
}
