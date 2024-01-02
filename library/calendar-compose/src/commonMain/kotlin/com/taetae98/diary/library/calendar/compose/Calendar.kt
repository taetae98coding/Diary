package com.taetae98.diary.library.calendar.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.taetae98.diary.library.calendar.compose.model.DateRange
import com.taetae98.diary.library.calendar.compose.month.Month
import com.taetae98.diary.library.calendar.compose.provider.LocalWeekSaturdayColor
import com.taetae98.diary.library.calendar.compose.provider.LocalWeekSundayColor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.DayOfWeek

@Composable
public fun Calendar(
    modifier: Modifier = Modifier,
    state: CalendarState,
    primaryDateRange: State<ImmutableList<DateRange>> = remember { mutableStateOf(persistentListOf()) }
) {
    Column(
        modifier = modifier,
    ) {
        DayOfWeek()
        Content(
            modifier = Modifier.weight(1F),
            state = state,
            primaryDateRange = primaryDateRange,
        )
    }
}

@Composable
private fun DayOfWeek(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {
        repeat(7) {
            val dayOfWeek = when (it) {
                0 -> DayOfWeek.SUNDAY
                else -> DayOfWeek(it)
            }

            val text = when (dayOfWeek) {
                DayOfWeek.SUNDAY -> "일"
                DayOfWeek.MONDAY -> "월"
                DayOfWeek.TUESDAY -> "화"
                DayOfWeek.WEDNESDAY -> "수"
                DayOfWeek.THURSDAY -> "목"
                DayOfWeek.FRIDAY -> "금"
                DayOfWeek.SATURDAY -> "토"
                else -> ""
            }

            val color = when (dayOfWeek) {
                DayOfWeek.SUNDAY -> LocalWeekSundayColor.current
                DayOfWeek.SATURDAY -> LocalWeekSaturdayColor.current
                else -> LocalContentColor.current
            }

            Text(
                modifier = Modifier.weight(1F),
                text = text,
                color = color,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(
    modifier: Modifier = Modifier,
    state: CalendarState,
    primaryDateRange: State<ImmutableList<DateRange>>,
) {
    HorizontalPager(
        modifier = modifier,
        state = state.pagerState,
        key = { it }
    ) {
        Month(
            modifier = Modifier.fillMaxSize(),
            state = state.getMonthState(it),
            primaryDateRange = primaryDateRange,
        )
    }
}
