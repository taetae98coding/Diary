package io.github.taetae98coding.diary.feature.calendar

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import io.github.taetae98coding.diary.core.compose.dialog.transparentScrimDialogProperties
import io.github.taetae98coding.diary.core.navigation.calendar.CalendarDestination
import io.github.taetae98coding.diary.core.navigation.calendar.CalendarFilterDestination
import io.github.taetae98coding.diary.core.navigation.calendar.CalendarHomeDestination
import io.github.taetae98coding.diary.core.navigation.memo.MemoAddDestination
import io.github.taetae98coding.diary.core.navigation.memo.MemoDetailDestination
import io.github.taetae98coding.diary.feature.calendar.filter.CalendarFilterRoute
import io.github.taetae98coding.diary.feature.calendar.home.CalendarHomeRoute

public fun NavGraphBuilder.calendarNavigation(
	navController: NavController,
) {
	navigation<CalendarDestination>(
		startDestination = CalendarHomeDestination,
	) {
		composable<CalendarHomeDestination> {
			CalendarHomeRoute(
				navigateToCalendarFilter = { navController.navigate(CalendarFilterDestination) },
				navigateToMemoAdd = { navController.navigate(MemoAddDestination(it.start, it.endInclusive)) },
				navigateToMemoDetail = {
					navController.navigate(
						route = MemoDetailDestination(it),
						navOptions = navOptions { launchSingleTop = true },
					)
				},
			)
		}

		dialog<CalendarFilterDestination>(
			dialogProperties = transparentScrimDialogProperties(),
		) {
			CalendarFilterRoute(
				navigateUp = navController::popBackStack,
			)
		}
	}
}
