package com.taetae98.diary.library.compose.calendar.week

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.taetae98.diary.library.compose.calendar.CalendarState
import com.taetae98.diary.library.compose.calendar.model.DateRange
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.plus

@Composable
internal fun WeekBackground(
    modifier: Modifier = Modifier,
    state: WeekState,
    calendarState: CalendarState,
) {
    val dateRange by remember {
        derivedStateOf {
            val range = calendarState.selectDateRange ?: return@derivedStateOf null

            DateRange(
                start = maxOf(state.start.minus(1, DateTimeUnit.DAY), range.start),
                endInclusive = minOf(state.endInclusive.plus(1, DateTimeUnit.DAY), range.endInclusive)
            )
        }
    }

    dateRange?.let {
        if (!(it.endInclusive < state.start || it.start > state.endInclusive)) {
            BackgroundLayout(
                modifier = modifier,
                state = state,
                dateRange = it
            )
        }
    }
}

@Composable
private fun BackgroundLayout(
    modifier: Modifier = Modifier,
    state: WeekState,
    dateRange: DateRange,
) {
    Row(
        modifier = modifier,
    ) {
        repeat(7) {
            Background(
                modifier = Modifier.weight(1F)
                    .fillMaxHeight(),
                state = state.weekDayState[it],
                dateRange = dateRange,
            )
        }
    }
}

@Composable
private fun Background(
    modifier: Modifier = Modifier,
    state: WeekDayState,
    dateRange: DateRange,
) {
    Box(
        modifier = modifier.drawBehind {
            if (state.localDate !in dateRange) return@drawBehind

            val path = Path().apply {
                val topLeft = if (state.localDate == dateRange.start) {
                    CornerRadius(50F, 50F)
                } else {
                    CornerRadius.Zero
                }
                val bottomRight = if (state.localDate == dateRange.endInclusive) {
                    CornerRadius(50F, 50F)
                } else {
                    CornerRadius.Zero
                }
                val roundRect = RoundRect(
                    rect = Rect(offset = Offset.Zero, size = size),
                    topLeft = topLeft,
                    bottomRight = bottomRight,
                )

                addRoundRect(roundRect)
            }

            drawPath(path = path, color = Color.Gray, alpha = 0.38F)
        }
    )
}
