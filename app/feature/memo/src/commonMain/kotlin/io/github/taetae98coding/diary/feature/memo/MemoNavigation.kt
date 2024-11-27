package io.github.taetae98coding.diary.feature.memo

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import io.github.taetae98coding.diary.core.navigation.memo.MemoAddDestination
import io.github.taetae98coding.diary.core.navigation.memo.MemoDestination
import io.github.taetae98coding.diary.core.navigation.memo.MemoDetailDestination
import io.github.taetae98coding.diary.core.navigation.tag.TagAddDestination
import io.github.taetae98coding.diary.core.navigation.tag.TagDetailDestination
import io.github.taetae98coding.diary.feature.memo.add.MemoAddRoute
import io.github.taetae98coding.diary.feature.memo.detail.MemoDetailRoute
import io.github.taetae98coding.diary.library.navigation.LocalDateNavType
import kotlinx.datetime.LocalDate
import kotlin.reflect.typeOf

public fun NavGraphBuilder.memoNavigation(
	navController: NavController,
) {
	navigation<MemoDestination>(
		startDestination = MemoAddDestination(),
	) {
		composable<MemoAddDestination>(
			typeMap = mapOf(typeOf<LocalDate?>() to LocalDateNavType),
		) {
			MemoAddRoute(
				navigateUp = navController::popBackStack,
				navigateToTagAdd = { navController.navigate(TagAddDestination) },
				navigateToTagDetail = {
					navController.navigate(
						route = TagDetailDestination(it),
						navOptions = navOptions { launchSingleTop = true },
					)
				},
			)
		}

		composable<MemoDetailDestination> {
			MemoDetailRoute(
				navigateUp = navController::popBackStack,
				navigateToTagAdd = { navController.navigate(TagAddDestination) },
				navigateToTagDetail = {
					navController.navigate(
						route = TagDetailDestination(it),
						navOptions = navOptions { launchSingleTop = true },
					)
				},
			)
		}
	}
}
