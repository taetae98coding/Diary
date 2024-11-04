package io.github.taetae98coding.diary.feature.calendar

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.taetae98coding.diary.core.navigation.calendar.CalendarDestination
import io.github.taetae98coding.diary.core.navigation.memo.MemoAddDestination
import io.github.taetae98coding.diary.core.navigation.memo.MemoDetailDestination

public fun NavGraphBuilder.calendarNavigation(
    navController: NavController,
) {
    composable<CalendarDestination> {
        CalendarRoute(
            navigateToMemoAdd = { navController.navigate(MemoAddDestination(it.start, it.endInclusive)) },
            navigateToMemoDetail = { navController.navigate(MemoDetailDestination(it)) },
        )
    }
}
