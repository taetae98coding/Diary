package com.taetae98.diary.library.calendar.compose.week

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.taetae98.diary.library.calendar.compose.provider.LocalWeekSaturdayColor
import com.taetae98.diary.library.calendar.compose.provider.LocalWeekSundayColor
import kotlinx.datetime.DayOfWeek

@Composable
public fun WeekDay(
    modifier: Modifier = Modifier,
    state: WeekDayState,
) {
    val color = when (state.dayOfWeek) {
        DayOfWeek.SUNDAY -> LocalWeekSundayColor.current
        DayOfWeek.SATURDAY -> LocalWeekSaturdayColor.current
        else -> LocalContentColor.current
    }.copy(
        alpha = if (state.isSameMonth()) {
            1F
        } else {
            0.38F
        }
    )

    Text(
        modifier = modifier,
        text = state.localDate.dayOfMonth.toString(),
        color = color,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleSmall
    )
}
