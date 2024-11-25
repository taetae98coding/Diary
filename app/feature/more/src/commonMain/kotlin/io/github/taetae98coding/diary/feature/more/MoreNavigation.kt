package io.github.taetae98coding.diary.feature.more

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.taetae98coding.diary.core.navigation.account.JoinDestination
import io.github.taetae98coding.diary.core.navigation.account.LoginDestination
import io.github.taetae98coding.diary.core.navigation.more.MoreDestination

public fun NavGraphBuilder.moreNavigation(
	navController: NavController,
) {
	composable<MoreDestination> {
		MoreRoute(
			navigateToLogin = { navController.navigate(LoginDestination) },
			navigateToJoin = { navController.navigate(JoinDestination) },
		)
	}
}
