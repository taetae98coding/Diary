package io.github.taetae98coding.diary.compose.calendar.week

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDateRange

public interface CalendarWeekGridScope {
    public fun item(
        key: Any? = null,
        contentType: Any? = null,
        localDateRange: LocalDateRange,
        content: @Composable CalendarWeekGridItemScope.() -> Unit,
    )

    public fun <T> items(
        items: List<T>,
        key: (T) -> Any? = { null },
        contentType: (T) -> Any? = { null },
        localDateRange: (T) -> LocalDateRange,
        content: @Composable CalendarWeekGridItemScope.(T) -> Unit,
    ) {
        items.forEach {
            item(
                key = key(it),
                contentType = contentType(it),
                localDateRange = localDateRange(it),
                content = { content(it) },
            )
        }
    }

    public fun items(
        count: Int,
        key: (Int) -> Any? = { null },
        contentType: (Int) -> Any? = { null },
        localDateRange: (Int) -> LocalDateRange,
        content: @Composable CalendarWeekGridItemScope.(Int) -> Unit,
    ) {
        repeat(count) {
            item(
                key = key(it),
                contentType = contentType(it),
                localDateRange = localDateRange(it),
                content = { content(it) },
            )
        }
    }

    public fun appendLine()
}
