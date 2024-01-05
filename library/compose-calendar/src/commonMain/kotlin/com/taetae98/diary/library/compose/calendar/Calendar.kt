package com.taetae98.diary.library.compose.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.taetae98.diary.library.compose.calendar.ext.toChristDayNumber
import com.taetae98.diary.library.compose.calendar.model.DateRange
import com.taetae98.diary.library.compose.calendar.month.Month
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

@Composable
public fun Calendar(
    modifier: Modifier = Modifier,
    state: CalendarState,
    primaryDate: State<ImmutableList<DateRange>> = remember { mutableStateOf(persistentListOf()) },
    schedule: State<ImmutableList<CalendarItem.Schedule>> = remember { mutableStateOf(persistentListOf()) },
    holiday: State<ImmutableList<CalendarItem.Holiday>> = remember { mutableStateOf(persistentListOf()) },
) {
    Column(
        modifier = modifier,
    ) {
        CalendarDayOfWeek()
        Content(
            modifier = Modifier.weight(1F),
            state = state,
            primaryDate = primaryDate,
            schedule = schedule,
            holiday = holiday,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(
    modifier: Modifier = Modifier,
    state: CalendarState,
    primaryDate: State<ImmutableList<DateRange>>,
    schedule: State<ImmutableList<CalendarItem.Schedule>>,
    holiday: State<ImmutableList<CalendarItem.Holiday>>,
) {
    val coroutineScope = rememberCoroutineScope()
    val onScrollPrev: () -> Unit by remember {
        mutableStateOf({
            coroutineScope.launch {
                state.scrollToBackward()
            }
        })
    }
    val onScrollNext: () -> Unit by remember {
        mutableStateOf({
            coroutineScope.launch {
                state.scrollToForward()
            }
        })
    }

    HorizontalPager(
        modifier = modifier.pointerInput(state.pagerState) {
            var touchPoint: LocalDate? = null

            detectDragGesturesAfterLongPress(
                onDragStart = {
                    val row = (it.y * 6 / size.height).toInt()
                    val column = (it.x * 7 / size.width).toInt()
                    val monthDate = LocalDate(state.currentYear, state.currentMonth, 1)

                    touchPoint = monthDate.minus(monthDate.dayOfWeek.toChristDayNumber(), DateTimeUnit.DAY)
                        .plus(row, DateTimeUnit.WEEK)
                        .plus(column, DateTimeUnit.DAY)
                },
                onDragEnd = {
                    state.selectDateRange = null
                },
                onDragCancel = {
                    state.selectDateRange = null
                },
                onDrag = { change, _ ->
                    val row = (change.position.y * 6 / size.height)
                    val column = (change.position.x * 7 / size.width)
                    val monthDate = LocalDate(state.currentYear, state.currentMonth, 1)
                    val dragDate = monthDate.minus(monthDate.dayOfWeek.toChristDayNumber(), DateTimeUnit.DAY)
                        .plus(row.toInt(), DateTimeUnit.WEEK)
                        .plus(column.toInt(), DateTimeUnit.DAY)
                    val baseDate = touchPoint ?: dragDate

                    if (!state.pagerState.isScrollInProgress) {
                        if (column <= 0.5F) {
                            onScrollPrev()
                        } else if (column >= 6.5F) {
                            onScrollNext()
                        }
                    }

                    state.selectDateRange = DateRange(
                        start = minOf(dragDate, baseDate),
                        endInclusive = maxOf(dragDate, baseDate),
                    )
                }
            )
        },
        state = state.pagerState,
        key = { it }
    ) {
        Month(
            modifier = Modifier.fillMaxSize(),
            state = state.getMonthState(it),
            calendarState = state,
            primaryDate = primaryDate,
            schedule = schedule,
            holiday = holiday,
        )
    }
}
