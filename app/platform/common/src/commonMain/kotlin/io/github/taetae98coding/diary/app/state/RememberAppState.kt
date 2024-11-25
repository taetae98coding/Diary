package io.github.taetae98coding.diary.app.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController

@Composable
internal fun rememberAppState(): AppState {
	val navController = rememberNavController()

	return remember(navController) {
		AppState(
			navController = navController,
		)
	}
}
