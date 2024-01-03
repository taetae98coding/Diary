package com.taetae98.diary.library.compose.calendar.week

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.style.TextAlign
import com.taetae98.diary.library.compose.calendar.CalendarItem
import com.taetae98.diary.library.compose.calendar.ext.toChristDayNumber
import com.taetae98.diary.library.compose.calendar.provider.LocalWeekSundayColor
import com.taetae98.diary.library.compose.color.toContrastColor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

@Composable
internal fun CalendarItemLayout(
    modifier: Modifier,
    state: WeekState,
    holiday: State<ImmutableList<CalendarItem>>,
) {
    val weekDayItem = remember {
        derivedStateOf {
            val holidayList = holiday.value
                .filterNot { it.endInclusive < state.start || it.start > state.endInclusive }
                .groupBy { it.name }
                .map { entry ->
                    CalendarItem.Holiday(
                        name = entry.key,
                        start = entry.value.minOf { it.start },
                        endInclusive = entry.value.maxOf { it.endInclusive }
                    )
                }
                .toMutableList()
            val list = buildList {
                while (holidayList.isNotEmpty()) {
                    val row = buildList {
                        addAll(
                            weekStart = state.start,
                            weekEndInclusive = state.endInclusive,
                            iterator = holidayList.iterator(),
                        )
                    }

                    add(row.toImmutableList())
                }
            }

            list.toImmutableList()
        }
    }

    WeekdayItemLayout(
        modifier = modifier,
        weekDayItem = weekDayItem
    )
}

@Composable
private fun WeekdayItemLayout(
    modifier: Modifier = Modifier,
    weekDayItem: State<ImmutableList<ImmutableList<WeekDayItem>>>
) {
    Column(
        modifier = modifier.verticalScroll(state = rememberScrollState()),
    ) {
        weekDayItem.value.forEach {
            WeekdayItemRow(weekDayItem = it)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WeekdayItemRow(
    modifier: Modifier = Modifier,
    weekDayItem: ImmutableList<WeekDayItem>
) {
    Row(
        modifier = modifier,
    ) {
        weekDayItem.forEach {
            when (it) {
                is WeekDayItem.Space -> {
                    Spacer(modifier = Modifier.weight(it.weight))
                }

                is WeekDayItem.Item -> {
                    key(it.key) {
                        val backgroundColor = it.color.takeOrElse { LocalWeekSundayColor.current }

                        Text(
                            modifier = Modifier.weight(it.weight)
                                .fillMaxHeight()
                                .background(backgroundColor)
                                .basicMarquee(iterations = Int.MAX_VALUE),
                            text = it.name,
                            color = backgroundColor.toContrastColor(),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

private fun MutableList<WeekDayItem>.addAll(
    weekStart: LocalDate,
    weekEndInclusive: LocalDate,
    iterator: MutableIterator<CalendarItem>
) {
    val sundayCursor = DayOfWeek.SUNDAY.toChristDayNumber()
    val saturdayCursor = DayOfWeek.SATURDAY.toChristDayNumber()
    var cursor = sundayCursor

    while (iterator.hasNext()) {
        val item = iterator.next()
        val itemStartCursor = if (item.start < weekStart) {
            sundayCursor
        } else {
            item.start.dayOfWeek.toChristDayNumber()
        }
        val itemEndCursor = if (item.endInclusive > weekEndInclusive) {
            saturdayCursor
        } else {
            item.endInclusive.dayOfWeek.toChristDayNumber()
        }

        if (cursor < itemStartCursor) {
            add(WeekDayItem.Space((itemStartCursor - cursor).toFloat()))
        }
        if (cursor <= itemStartCursor) {
            add(
                WeekDayItem.Item(
                    key = item.key,
                    name = item.name,
                    weight = itemEndCursor - itemStartCursor + 1F,
                    color = Color.Unspecified,
                )
            )

            iterator.remove()
            cursor = itemEndCursor + 1
            if (cursor > saturdayCursor) {
                break
            }
        }
    }

    if (cursor <= saturdayCursor) {
        add(WeekDayItem.Space(saturdayCursor - cursor + 1F))
    }
}
