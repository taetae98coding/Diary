package com.taetae98.diary.library.compose.calendar.week

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.taetae98.diary.library.compose.calendar.CalendarItem
import com.taetae98.diary.library.compose.calendar.model.DateRange
import com.taetae98.diary.library.compose.calendar.provider.LocalWeekSaturdayColor
import com.taetae98.diary.library.compose.calendar.provider.LocalWeekSundayColor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.DayOfWeek

@Composable
internal fun WeekDay(
    modifier: Modifier = Modifier,
    state: WeekDayState,
    primaryDate: State<ImmutableList<DateRange>>,
    holiday: State<ImmutableList<CalendarItem.Holiday>>,
) {
    val isPrimaryDate by remember {
        derivedStateOf { primaryDate.value.any { it.contains(state.localDate) } }
    }

    if (isPrimaryDate) {
        PrimaryWeekDay(
            modifier = modifier,
            state = state
        )
    } else {
        NormalWeekDay(
            modifier = modifier,
            state = state,
            holiday = holiday,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrimaryWeekDay(
    modifier: Modifier = Modifier,
    state: WeekDayState,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Badge(containerColor = MaterialTheme.colorScheme.primary) {
            WeekDayText(state = state)
        }
    }
}

@Composable
private fun NormalWeekDay(
    modifier: Modifier = Modifier,
    state: WeekDayState,
    holiday: State<ImmutableList<CalendarItem.Holiday>>,
) {
    val isHoliday by remember {
        derivedStateOf { holiday.value.any { it.contains(state.localDate) } }
    }

    val color = when  {
        state.dayOfWeek == DayOfWeek.SUNDAY || isHoliday -> LocalWeekSundayColor.current
        state.dayOfWeek == DayOfWeek.SATURDAY -> LocalWeekSaturdayColor.current
        else -> LocalContentColor.current
    }.copy(
        alpha = if (state.isSameMonth()) {
            1F
        } else {
            0.38F
        }
    )

    WeekDayText(
        modifier = modifier,
        state = state,
        color = color,
    )
}


@Composable
private fun WeekDayText(
    modifier: Modifier = Modifier,
    state: WeekDayState,
    color: Color = Color.Unspecified,
) {
    Text(
        modifier = modifier,
        text = state.localDate.dayOfMonth.toString(),
        color = color,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleSmall
    )
}