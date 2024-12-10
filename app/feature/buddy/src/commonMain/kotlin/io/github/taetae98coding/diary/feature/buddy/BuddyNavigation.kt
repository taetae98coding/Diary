package io.github.taetae98coding.diary.feature.buddy

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import io.github.taetae98coding.diary.core.compose.adaptive.isListVisible
import io.github.taetae98coding.diary.core.navigation.buddy.BuddyDestination
import io.github.taetae98coding.diary.core.navigation.buddy.BuddyGroupCalendarDestination
import io.github.taetae98coding.diary.core.navigation.buddy.BuddyGroupMemoAddDestination
import io.github.taetae98coding.diary.core.navigation.buddy.BuddyGroupMemoDetailDestination
import io.github.taetae98coding.diary.core.navigation.buddy.BuddyHomeDestination
import io.github.taetae98coding.diary.feature.buddy.calendar.BuddyGroupCalendarRoute
import io.github.taetae98coding.diary.feature.buddy.home.BuddyHomeRoute
import io.github.taetae98coding.diary.feature.buddy.memo.add.BuddyGroupMemoAddRoute
import io.github.taetae98coding.diary.feature.buddy.memo.detail.BuddyGroupMemoDetailRoute
import io.github.taetae98coding.diary.library.navigation.LocalDateNavType
import kotlin.reflect.typeOf
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
public fun NavGraphBuilder.buddyNavigation(
    navController: NavController,
) {
    navigation<BuddyDestination>(
        startDestination = BuddyHomeDestination,
    ) {
        composable<BuddyHomeDestination> { backStackEntry ->
            BuddyHomeRoute(
                navigateToBuddyCalendar = { navController.navigate(BuddyGroupCalendarDestination(it)) },
                onScaffoldValueChange = { backStackEntry.savedStateHandle["app_navigation_visible"] = it.isListVisible() },
            )
        }

        composable<BuddyGroupCalendarDestination> {
            val route = it.toRoute<BuddyGroupCalendarDestination>()

            BuddyGroupCalendarRoute(
                navigateUp = navController::popBackStack,
                navigateToBuddyGroupMemoAdd = { navController.navigate(BuddyGroupMemoAddDestination(route.groupId, it.start, it.endInclusive)) },
                navigateToBuddyGroupMemoDetail = {navController.navigate(BuddyGroupMemoDetailDestination(it))},
            )
        }

        composable<BuddyGroupMemoAddDestination>(
            typeMap = mapOf(typeOf<LocalDate?>() to LocalDateNavType),
        ) {
            BuddyGroupMemoAddRoute(
                navigateUp = navController::popBackStack,
            )
        }

        composable<BuddyGroupMemoDetailDestination> {
            BuddyGroupMemoDetailRoute(
                navigateUp = navController::popBackStack,
            )
        }
    }
}
