package io.github.taetae98coding.diary.feature.tag

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import io.github.taetae98coding.diary.core.compose.adaptive.isListVisible
import io.github.taetae98coding.diary.core.navigation.memo.MemoAddDestination
import io.github.taetae98coding.diary.core.navigation.memo.MemoDetailDestination
import io.github.taetae98coding.diary.core.navigation.tag.TagAddDestination
import io.github.taetae98coding.diary.core.navigation.tag.TagDestination
import io.github.taetae98coding.diary.core.navigation.tag.TagDetailDestination
import io.github.taetae98coding.diary.feature.tag.add.TagAddRoute
import io.github.taetae98coding.diary.feature.tag.detail.TagDetailRoute

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
public fun NavGraphBuilder.tagNavigation(
	navController: NavController,
) {
	composable<TagDestination> { backStackEntry ->
		TagRoute(
			navigateToMemoAdd = { navController.navigate(MemoAddDestination(selectedTag = it)) },
			navigateToMemoDetail = {
				navController.navigate(
					route = MemoDetailDestination(it),
					navOptions = navOptions { launchSingleTop = true },
				)
			},
			onScaffoldValueChange = { backStackEntry.savedStateHandle["app_navigation_visible"] = it.isListVisible() },
		)
	}

	composable<TagAddDestination> {
		TagAddRoute(
			navigateUp = navController::popBackStack,
		)
	}

	composable<TagDetailDestination> { backStackEntry ->
		val route = backStackEntry.toRoute<TagDetailDestination>()

		TagDetailRoute(
			navigateUp = navController::popBackStack,
			navigateToMemoAdd = { navController.navigate(MemoAddDestination(selectedTag = route.tagId)) },
			navigateToMemoDetail = {
				navController.navigate(
					route = MemoDetailDestination(it),
					navOptions = navOptions { launchSingleTop = true },
				)
			},
		)
	}
}
