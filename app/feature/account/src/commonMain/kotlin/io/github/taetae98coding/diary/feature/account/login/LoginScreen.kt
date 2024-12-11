package io.github.taetae98coding.diary.feature.account.login

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import io.github.taetae98coding.diary.core.design.system.emoji.Emoji
import io.github.taetae98coding.diary.core.design.system.icon.NavigateUpIcon
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.feature.account.common.BasePasswordTextField
import io.github.taetae98coding.diary.feature.account.common.BottomBarButton
import io.github.taetae98coding.diary.feature.account.common.EmailTextField
import io.github.taetae98coding.diary.feature.account.login.state.LoginScreenButtonUiState
import io.github.taetae98coding.diary.feature.account.login.state.LoginScreenState
import io.github.taetae98coding.diary.feature.account.login.state.LoginUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoginScreen(
	state: LoginScreenState,
	onNavigateUp: () -> Unit,
	onLogin: () -> Unit,
	uiStateProvider: () -> LoginUiState,
	onLoginFinish: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Scaffold(
		modifier = modifier,
		topBar = {
			TopAppBar(
				title = { Text(text = "로그인") },
				navigationIcon = {
					IconButton(onClick = onNavigateUp) {
						NavigateUpIcon()
					}
				},
			)
		},
		bottomBar = {
			val isEnable by remember {
				derivedStateOf { state.buttonState == LoginScreenButtonUiState.LoginEnable }
			}
			val isProgress by remember {
				derivedStateOf { uiStateProvider().isProgress }
			}

			BottomBarButton(
				onClick = onLogin,
				enableProvider = { isEnable },
				modifier = Modifier.fillMaxWidth(),
			) {
				LoginButtonContent(
					uiState = if (isProgress) {
						LoginScreenButtonUiState.Progress
					} else {
						state.buttonState
					},
				)
			}
		},
		snackbarHost = { SnackbarHost(hostState = state.hostState) },
		contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.navigationBars),
	) {
		Content(
			state = state,
			onLogin = onLogin,
			modifier = Modifier
				.padding(it)
				.padding(DiaryTheme.dimen.screenPaddingValues),
		)
	}

	Message(
		state = state,
		uiStateProvider = uiStateProvider,
		onLoginFinish = onLoginFinish,
	)
}

@Composable
private fun Message(
	state: LoginScreenState,
	uiStateProvider: () -> LoginUiState,
	onLoginFinish: () -> Unit,
) {
	val uiState = uiStateProvider()

	LaunchedEffect(
		uiState.isLoginFinish,
		uiState.isAccountNotFound,
		uiState.isNetworkError,
		uiState.isUnknownError,
	) {
		if (!uiState.hasMessage) return@LaunchedEffect

		when {
			uiState.isLoginFinish -> onLoginFinish()
			uiState.isAccountNotFound -> state.showMessage("계정을 찾을 수 없어요 ${Emoji.fail.random()}")
			uiState.isNetworkError -> state.showMessage("네트워크 상태를 확인해 주세요 ${Emoji.fail.random()}")
			uiState.isUnknownError -> state.showMessage("알 수 없는 에러가 발생했어요 잠시 후 다시 시도해 주세요 ${Emoji.error.random()}")
		}

		uiState.onMessageShow()
	}
}

@Composable
private fun LoginButtonContent(
	uiState: LoginScreenButtonUiState,
	modifier: Modifier = Modifier,
) {
	when (uiState) {
		LoginScreenButtonUiState.LoginEnable -> {
			Text(
				text = "로그인",
				modifier = modifier,
			)
		}

		LoginScreenButtonUiState.EmailBlank -> {
			Text(
				text = "이메일을 입력해 주세요 🐸",
				modifier = modifier,
			)
		}

		LoginScreenButtonUiState.PasswordBlank -> {
			Text(
				text = "비밀번호를 입력해 주세요 🐷",
				modifier = modifier,
			)
		}

		LoginScreenButtonUiState.Progress -> {
			CircularProgressIndicator(
				modifier = modifier,
				color = LocalContentColor.current,
			)
		}
	}
}

@Composable
private fun Content(
	state: LoginScreenState,
	onLogin: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Card(modifier = modifier) {
		val textFieldModifier = Modifier.fillMaxWidth()

		EmailTextField(
			valueProvider = { state.email },
			onValueChange = state::onEmailChange,
			modifier = textFieldModifier,
		)

		BasePasswordTextField(
			valueProvider = { state.password },
			onValueChange = state::onPasswordChange,
			modifier = textFieldModifier,
			placeholder = { Text(text = "비밀번호 확인") },
			passwordVisibleProvider = { state.isPasswordVisible },
			onPasswordVisibleChange = state::onPasswordVisibleChange,
			keyboardOptions = KeyboardOptions(
				capitalization = KeyboardCapitalization.None,
				keyboardType = KeyboardType.Password,
				imeAction = ImeAction.Done,
			),
			keyboardActions = KeyboardActions(
				onAny = {
					if (state.buttonState == LoginScreenButtonUiState.LoginEnable) {
						onLogin()
					}
				},
			),
		)
	}
}
