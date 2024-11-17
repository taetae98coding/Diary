package io.github.taetae98coding.diary.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import io.github.taetae98coding.diary.app.state.AppState
import io.github.taetae98coding.diary.core.navigation.calendar.CalendarDestination
import io.github.taetae98coding.diary.feature.account.accountNavigation
import io.github.taetae98coding.diary.feature.calendar.calendarNavigation
import io.github.taetae98coding.diary.feature.memo.memoNavigation
import io.github.taetae98coding.diary.feature.more.moreNavigation
import io.github.taetae98coding.diary.feature.tag.tagNavigation

@Composable
internal fun AppNavHost(
    state: AppState,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = state.navController,
        startDestination = CalendarDestination,
        modifier = modifier,
    ) {
        memoNavigation(navController = state.navController)
        tagNavigation(navController = state.navController)
        calendarNavigation(navController = state.navController)
        moreNavigation(navController = state.navController)
        accountNavigation(navController = state.navController)
    }
}
