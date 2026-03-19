package io.github.taetae98coding.diary.compose.calendar.week

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDateRange

internal data class CalendarWeekGridItem(
    val key: Any?,
    val contentType: Any?,
    val localDateRange: LocalDateRange,
    val content: @Composable (CalendarWeekGridItemScope.() -> Unit),
)
