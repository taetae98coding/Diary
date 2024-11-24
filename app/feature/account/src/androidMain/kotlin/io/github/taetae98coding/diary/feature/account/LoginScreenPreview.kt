package io.github.taetae98coding.diary.feature.account

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.feature.account.login.LoginScreen
import io.github.taetae98coding.diary.feature.account.login.state.LoginUiState
import io.github.taetae98coding.diary.feature.account.login.state.rememberLoginScreenState

@Composable
@DiaryPreview
private fun LoginScreenPreview() {
	DiaryTheme {
		LoginScreen(
			state = rememberLoginScreenState(),
			onNavigateUp = {},
			onLogin = {},
			uiStateProvider = { LoginUiState() },
			onLoginFinish = {},
		)
	}
}
