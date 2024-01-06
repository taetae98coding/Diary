package com.taetae98.diary.library.compose.calendar.week

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
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
import com.taetae98.diary.library.compose.calendar.provider.LocalWeekSundayColor
import com.taetae98.diary.library.compose.color.toContrastColor
import com.taetae98.diary.library.kotlin.ext.toChristDayNumber
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

@Composable
internal fun CalendarItemLayout(
    modifier: Modifier,
    state: WeekState,
    schedule: State<ImmutableList<CalendarItem.Schedule>>,
    holiday: State<ImmutableList<CalendarItem>>,
    onHoliday: (key: Any) -> Unit,
) {
    val weekDayItem = remember {
        derivedStateOf {
            val scheduleList = schedule.value
                .filter { it.endInclusive >= state.start && it.start <= state.endInclusive }
                .toMutableList()

            val holidayList = holiday.value
                .filter { it.endInclusive >= state.start && it.start <= state.endInclusive }
                .groupBy { it.name }
                .map { entry ->
                    CalendarItem.Holiday(
                        name = entry.key,
                        start = entry.value.minOf { it.start },
                        endInclusive = entry.value.maxOf { it.endInclusive }
                    )
                }
                .toMutableList()

            val itemList = buildList {
                addAll(scheduleList)
                addAll(holidayList)
            }.sortedWith { a, b ->
                if (a.start != b.start) {
                    compareValues(a.start, b.start)
                } else {
                    -compareValues(a.endInclusive, b.endInclusive)
                }
            }.toMutableList()

            val list = buildList {
                while (itemList.isNotEmpty()) {
                    val row = buildList {
                        addAll(
                            weekStart = state.start,
                            weekEndInclusive = state.endInclusive,
                            iterator = itemList.iterator(),
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
        weekDayItem = weekDayItem,
        onHoliday = onHoliday,
    )
}

@Composable
private fun WeekdayItemLayout(
    modifier: Modifier = Modifier,
    weekDayItem: State<ImmutableList<ImmutableList<WeekDayItem>>>,
    onHoliday: (key: Any) -> Unit,
) {
    Column(
        modifier = modifier.verticalScroll(state = rememberScrollState()),
    ) {
        weekDayItem.value.forEach {
            WeekdayItemRow(
                weekDayItem = it,
                onHoliday = onHoliday,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WeekdayItemRow(
    modifier: Modifier = Modifier,
    weekDayItem: ImmutableList<WeekDayItem>,
    onHoliday: (key: Any) -> Unit,
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
                                .clickable { onHoliday(it.key) }
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
    var cursor = sumOf { it.weight.toInt() }

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
            val color = if (item is CalendarItem.Schedule) {
                Color(item.color)
            } else {
                Color.Unspecified
            }

            add(
                WeekDayItem.Item(
                    key = item.key,
                    name = item.name,
                    weight = itemEndCursor - itemStartCursor + 1F,
                    color = color,
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
