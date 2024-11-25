package io.github.taetae98coding.diary.feature.account

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.taetae98coding.diary.core.navigation.account.JoinDestination
import io.github.taetae98coding.diary.core.navigation.account.LoginDestination
import io.github.taetae98coding.diary.feature.account.join.JoinRoute
import io.github.taetae98coding.diary.feature.account.login.LoginRoute

public fun NavGraphBuilder.accountNavigation(
	navController: NavController,
) {
	composable<JoinDestination> {
		JoinRoute(
			navigateUp = navController::popBackStack,
		)
	}

	composable<LoginDestination> {
		LoginRoute(
			navigateUp = navController::popBackStack,
		)
	}
}
