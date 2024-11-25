package io.github.taetae98coding.diary.feature.account.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.feature.account.login.state.rememberLoginScreenState
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun LoginRoute(
	navigateUp: () -> Unit,
	modifier: Modifier = Modifier,
	viewModel: LoginViewModel = koinViewModel(),
) {
	val state = rememberLoginScreenState()
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	LoginScreen(
		state = state,
		onNavigateUp = navigateUp,
		onLogin = { viewModel.login(state.email, state.password) },
		uiStateProvider = { uiState },
		onLoginFinish = navigateUp,
		modifier = modifier,
	)
}
