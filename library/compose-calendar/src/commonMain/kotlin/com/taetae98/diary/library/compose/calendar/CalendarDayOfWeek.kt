package com.taetae98.diary.library.compose.calendar

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.taetae98.diary.library.compose.calendar.provider.LocalWeekSaturdayColor
import com.taetae98.diary.library.compose.calendar.provider.LocalWeekSundayColor

@Composable
public fun CalendarDayOfWeek(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {
        repeat(7) {
            val dayOfWeek = when (it) {
                0 -> kotlinx.datetime.DayOfWeek.SUNDAY
                else -> kotlinx.datetime.DayOfWeek(it)
            }

            val text = when (dayOfWeek) {
                kotlinx.datetime.DayOfWeek.SUNDAY -> "일"
                kotlinx.datetime.DayOfWeek.MONDAY -> "월"
                kotlinx.datetime.DayOfWeek.TUESDAY -> "화"
                kotlinx.datetime.DayOfWeek.WEDNESDAY -> "수"
                kotlinx.datetime.DayOfWeek.THURSDAY -> "목"
                kotlinx.datetime.DayOfWeek.FRIDAY -> "금"
                kotlinx.datetime.DayOfWeek.SATURDAY -> "토"
                else -> ""
            }

            val color = when (dayOfWeek) {
                kotlinx.datetime.DayOfWeek.SUNDAY -> LocalWeekSundayColor.current
                kotlinx.datetime.DayOfWeek.SATURDAY -> LocalWeekSaturdayColor.current
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