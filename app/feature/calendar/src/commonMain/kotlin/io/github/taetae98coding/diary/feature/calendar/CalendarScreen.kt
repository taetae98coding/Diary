package io.github.taetae98coding.diary.feature.calendar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LifecycleStartEffect
import io.github.taetae98coding.diary.core.calendar.compose.Calendar
import io.github.taetae98coding.diary.core.calendar.compose.item.CalendarItemUiState
import io.github.taetae98coding.diary.core.calendar.compose.modifier.calendarDateRangeSelectable
import io.github.taetae98coding.diary.core.calendar.compose.topbar.CalendarTopBar
import io.github.taetae98coding.diary.library.datetime.todayIn
import kotlinx.datetime.LocalDate

@Composable
internal fun CalendarScreen(
    state: CalendarScreenState,
    onSelectDate: (ClosedRange<LocalDate>) -> Unit,
    textItemListProvider: () -> List<CalendarItemUiState.Text>,
    holidayListProvider: () -> List<CalendarItemUiState.Holiday>,
    onCalendarItemClick: (Any) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { CalendarTopBar(state = state.calendarState) },
    ) {
        var today by remember { mutableStateOf(LocalDate.todayIn()) }

        Calendar(
            state = state.calendarState,
            primaryDateListProvider = { listOf(today) },
            textItemListProvider = textItemListProvider,
            holidayListProvider = holidayListProvider,
            onCalendarItemClick = onCalendarItemClick,
            modifier = Modifier.fillMaxSize()
                .padding(it)
                .calendarDateRangeSelectable(
                    state = state.calendarState,
                    onSelectDate = onSelectDate,
                ),
        )

        LifecycleStartEffect(Unit) {
            today = LocalDate.todayIn()
            onStopOrDispose { }
        }
    }
}
