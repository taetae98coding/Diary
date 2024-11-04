package io.github.taetae98coding.diary.core.calendar.compose.week

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.calendar.compose.CalendarDefaults
import io.github.taetae98coding.diary.core.calendar.compose.color.CalendarColors
import io.github.taetae98coding.diary.core.calendar.compose.item.CalendarItemUiState
import io.github.taetae98coding.diary.library.color.multiplyAlpha
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil

@Composable
internal fun CalendarWeek(
    state: CalendarWeekState,
    primaryDateListProvider: () -> List<LocalDate>,
    textItemListProvider: () -> List<CalendarItemUiState.Text>,
    holidayListProvider: () -> List<CalendarItemUiState.Holiday>,
    onCalendarItemClick: (Any) -> Unit,
    modifier: Modifier = Modifier,
    colors: CalendarColors = CalendarDefaults.colors(),
) {
    val selectColor = colors.selectColor.takeOrElse { LocalContentColor.current.multiplyAlpha(0.2F) }

    Column(
        modifier = modifier.drawBehind {
            val dateRange = state.selectedDateRange ?: return@drawBehind
            val startWeight = state.dateRange.start.daysUntil(dateRange.start)
            val endWeight = dateRange.endInclusive.daysUntil(state.dateRange.endInclusive)

            drawRect(
                color = selectColor,
                topLeft = Offset(size.width / 7 * startWeight, 0F),
                size = Size(size.width * (1F - (1F / 7F * (startWeight + endWeight))), size.height),
            )
        },
    ) {
        HorizontalDivider(thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(2.dp))
        CalendarDayOfMonthRow(
            state = state,
            primaryDateListProvider = primaryDateListProvider,
            holidayListProvider = holidayListProvider,
            colors = colors,
        )
        CalendarItemVerticalGrid(
            state = state,
            textItemListProvider = textItemListProvider,
            holidayListProvider = holidayListProvider,
            onCalendarItemClick = onCalendarItemClick,
            modifier = Modifier.fillMaxSize(),
            colors = colors,
        )
    }
}
