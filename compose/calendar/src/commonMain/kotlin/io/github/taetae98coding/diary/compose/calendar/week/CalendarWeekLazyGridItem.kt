package io.github.taetae98coding.diary.compose.calendar.week

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.runtime.Composable

internal data class CalendarWeekLazyGridItem(
    val key: Any?,
    val span: LazyGridItemSpanScope.() -> GridItemSpan,
    val contentType: Any?,
    val content: @Composable (CalendarWeekGridItemScope.() -> Unit),
)
