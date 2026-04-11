package io.github.taetae98coding.diary.compose.calendar.week

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.YearMonth

@Composable
internal fun CalendarWeekGrid(
    yearMonth: YearMonth,
    weekOfMonth: Int,
    modifier: Modifier = Modifier,
    content: CalendarWeekGridScope.() -> Unit = {},
) {
    val gridScope = CalendarWeekGridScopeImpl(yearMonth, weekOfMonth).apply(content)
    val gridItems by remember(gridScope.rows) {
        derivedStateOf {
            gridScope.rows.flatMap(CalendarWeekGridItemMapper(yearMonth, weekOfMonth)::mapToCalendarWeekLazyGridItem)
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = modifier,
        contentPadding = PaddingValues(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        gridItems.forEach {
            item(
                key = it.key,
                span = it.span,
                contentType = it.contentType,
            ) {
                it.content(CalendarWeekLazyGridItemScope(this))
            }
        }
    }
}
