package com.taetae98.diary.feature.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.koin.navigation.compose.koinInject
import com.taetae98.diary.navigation.core.calendar.CalendarEntry

@Composable
@NonRestartableComposable
public fun CalendarEntryPoint(
    modifier: Modifier = Modifier,
    entry: CalendarEntry,
) {
    CalendarRoute(
        modifier = modifier,
        viewModel = entry.koinInject(),
        navigateToMemoAdd = entry.navigateToMemoAdd,
        navigateToMemoDetail = entry.navigateToMemoDetail,
    )
}
