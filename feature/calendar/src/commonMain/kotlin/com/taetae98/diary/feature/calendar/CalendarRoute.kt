package com.taetae98.diary.feature.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.calendar.compose.runtime.rememberCalendarState

@Composable
internal fun CalendarRoute(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel,
) {
    val state = rememberCalendarState()

    CalendarScreen(
        modifier = modifier,
        state = state,
        holiday = viewModel.holiday.collectAsState()
    )

    LaunchedEffect(state.currentYear, state.currentMonth) {
        viewModel.setYearAndMonth(state.currentYear, state.currentMonth)
    }
}