package io.github.taetae98coding.diary.core.calendar.compose.week

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.core.calendar.compose.CalendarDefaults
import io.github.taetae98coding.diary.core.calendar.compose.color.CalendarColors
import io.github.taetae98coding.diary.core.calendar.compose.day.CalendarDayOfMonth
import io.github.taetae98coding.diary.core.calendar.compose.day.CalendarDayOfMonthState
import io.github.taetae98coding.diary.core.calendar.compose.item.CalendarItemUiState
import io.github.taetae98coding.diary.library.datetime.toChristDayOfWeek
import kotlinx.datetime.LocalDate

@Composable
internal fun CalendarDayOfMonthRow(
    state: CalendarWeekState,
    primaryDateListProvider: () -> List<LocalDate>,
    holidayListProvider: () -> List<CalendarItemUiState.Holiday>,
    modifier: Modifier = Modifier,
    colors: CalendarColors = CalendarDefaults.colors(),
) {
    Row(modifier = modifier) {
        val dayOfMonthModifier = Modifier.weight(1F)

        repeat(7) { dayOfWeek ->
            val dayOfMonthState = remember {
                CalendarDayOfMonthState(
                    year = state.year,
                    month = state.month,
                    weekOfMonth = state.weekOfMonth,
                    dayOfWeek = dayOfWeek.toChristDayOfWeek(),
                )
            }

            CalendarDayOfMonth(
                state = dayOfMonthState,
                primaryDateListProvider = primaryDateListProvider,
                holidayListProvider = holidayListProvider,
                modifier = dayOfMonthModifier,
                colors = colors,
            )
        }
    }
}
