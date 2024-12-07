package io.github.taetae98coding.diary.feature.buddy

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import io.github.taetae98coding.diary.core.compose.adaptive.isListVisible
import io.github.taetae98coding.diary.core.navigation.buddy.BuddyDestination
import io.github.taetae98coding.diary.core.navigation.buddy.BuddyHomeDestination
import io.github.taetae98coding.diary.feature.buddy.home.BuddyHomeRoute

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
public fun NavGraphBuilder.buddyNavigation(
	navController: NavController,
) {
	navigation<BuddyDestination>(
		startDestination = BuddyHomeDestination,
	) {
		composable<BuddyHomeDestination> { backStackEntry ->
			BuddyHomeRoute(
				onScaffoldValueChange = { backStackEntry.savedStateHandle["app_navigation_visible"] = it.isListVisible() },
			)
		}
	}
}
