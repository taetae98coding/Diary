package io.github.taetae98coding.diary.feature.tag

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.taetae98coding.diary.core.compose.adaptive.isListVisible
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
			onScaffoldValueChange = { backStackEntry.savedStateHandle["app_navigation_visible"] = it.isListVisible() },
		)
	}

	composable<TagAddDestination> {
		TagAddRoute(
			navigateUp = navController::popBackStack,
		)
	}

	composable<TagDetailDestination> {
		TagDetailRoute(
			navigateUp = navController::popBackStack,
		)
	}
}
