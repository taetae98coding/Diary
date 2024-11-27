package io.github.taetae98coding.diary.app.state

import androidx.compose.runtime.Stable
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import io.github.taetae98coding.diary.app.navigation.AppNavigation
import io.github.taetae98coding.diary.core.navigation.calendar.CalendarHomeDestination

@Stable
internal class AppState(
	val navController: NavHostController,
) {
	fun navigate(navigation: AppNavigation) {
		val isSelected =
			navController.currentBackStackEntry
				?.destination
				?.hierarchy
				?.any { it.hasRoute(navigation.route::class) }
				?: false

		if (!isSelected) {
			navController.navigate(navigation.route) {
				popUpTo(CalendarHomeDestination) {
					saveState = true
				}

				restoreState = true
				launchSingleTop = true
			}
		}
	}
}
