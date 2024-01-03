package com.taetae98.diary.feature.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier

@Composable
internal fun CalendarRoute(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel,
) {
    CalendarScreen(
        modifier = modifier,
        holiday = viewModel.holiday.collectAsState()
    )
}