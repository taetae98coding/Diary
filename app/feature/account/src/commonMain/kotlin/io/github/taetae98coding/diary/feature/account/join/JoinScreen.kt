package io.github.taetae98coding.diary.feature.account.join

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
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
import io.github.taetae98coding.diary.feature.account.join.state.JoinScreenButtonUiState
import io.github.taetae98coding.diary.feature.account.join.state.JoinScreenState
import io.github.taetae98coding.diary.feature.account.join.state.JoinUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun JoinScreen(
	state: JoinScreenState,
	onNavigateUp: () -> Unit,
	onJoin: () -> Unit,
	uiStateProvider: () -> JoinUiState,
	onLoginFinish: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Scaffold(
		modifier = modifier,
		topBar = {
			TopAppBar(
				title = { Text(text = "회원가입") },
				navigationIcon = {
					IconButton(onClick = onNavigateUp) {
						NavigateUpIcon()
					}
				},
			)
		},
		bottomBar = {
			val isEnable by remember {
				derivedStateOf { state.buttonState == JoinScreenButtonUiState.JoinEnable }
			}
			val isProgress by remember {
				derivedStateOf { uiStateProvider().isProgress }
			}

			BottomBarButton(
				onClick = onJoin,
				enableProvider = { isEnable },
				modifier = Modifier.fillMaxWidth(),
			) {
				JoinButtonContent(
					uiState = if (isProgress) {
						JoinScreenButtonUiState.Progress
					} else {
						state.buttonState
					},
				)
			}
		},
		snackbarHost = { SnackbarHost(hostState = state.hostState) },
	) {
		Content(
			state = state,
			onJoin = onJoin,
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
private fun JoinButtonContent(
	uiState: JoinScreenButtonUiState,
	modifier: Modifier = Modifier,
) {
	when (uiState) {
		JoinScreenButtonUiState.JoinEnable -> {
			Text(
				text = "회원가입",
				modifier = modifier,
			)
		}

		JoinScreenButtonUiState.EmailBlank -> {
			Text(
				text = "이메일을 입력해 주세요 🐮",
				modifier = modifier,
			)
		}

		JoinScreenButtonUiState.PasswordBlank -> {
			Text(
				text = "비밀번호를 입력해 주세요 🦁",
				modifier = modifier,
			)
		}

		JoinScreenButtonUiState.InvalidEmail -> {
			Text(
				text = "이메일 형식을 지켜주세요 🐯",
				modifier = modifier,
			)
		}

		JoinScreenButtonUiState.PasswordDifferent -> {
			Text(
				text = "입력된 패스워드가 달라요 🐨",
				modifier = modifier,
			)
		}

		JoinScreenButtonUiState.Progress -> {
			CircularProgressIndicator(
				modifier = modifier,
				color = LocalContentColor.current,
			)
		}
	}
}

@Composable
private fun Content(
	state: JoinScreenState,
	onJoin: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Card(modifier = modifier) {
		val textFieldModifier = Modifier.fillMaxWidth()

		EmailTextField(
			valueProvider = { state.email },
			onValueChange = state::onEmailChange,
			modifier = textFieldModifier,
		)

		PasswordTextField(
			state = state,
			modifier = textFieldModifier,
		)
		CheckPasswordTextField(
			state = state,
			onJoin = onJoin,
			modifier = textFieldModifier,
		)
	}
}

@Composable
private fun PasswordTextField(
	state: JoinScreenState,
	modifier: Modifier = Modifier,
) {
	BasePasswordTextField(
		valueProvider = { state.password },
		onValueChange = state::onPasswordChange,
		modifier = modifier,
		placeholder = { Text(text = "패스워드") },
		passwordVisibleProvider = { state.isPasswordVisible },
		onPasswordVisibleChange = state::onPasswordVisibleChange,
		keyboardOptions = KeyboardOptions(
			capitalization = KeyboardCapitalization.None,
			keyboardType = KeyboardType.Password,
			imeAction = ImeAction.Next,
		),
	)
}

@Composable
private fun CheckPasswordTextField(
	state: JoinScreenState,
	onJoin: () -> Unit,
	modifier: Modifier = Modifier,
) {
	BasePasswordTextField(
		valueProvider = { state.checkPassword },
		onValueChange = state::onCheckPasswordChange,
		modifier = modifier,
		placeholder = { Text(text = "비밀번호 확인") },
		passwordVisibleProvider = { state.isCheckPasswordVisible },
		onPasswordVisibleChange = state::onCheckPasswordVisibleChange,
		keyboardOptions = KeyboardOptions(
			capitalization = KeyboardCapitalization.None,
			keyboardType = KeyboardType.Password,
			imeAction = ImeAction.Done,
		),
		keyboardActions = KeyboardActions(
			onAny = {
				if (state.buttonState == JoinScreenButtonUiState.JoinEnable) {
					onJoin()
				}
			},
		),
	)
}

@Composable
private fun Message(
	state: JoinScreenState,
	uiStateProvider: () -> JoinUiState,
	onLoginFinish: () -> Unit,
) {
	val uiState = uiStateProvider()

	LaunchedEffect(
		uiState.isLoginFinish,
		uiState.isExistEmail,
		uiState.isNetworkError,
		uiState.isUnknownError,
	) {
		if (!uiState.hasMessage) return@LaunchedEffect

		when {
			uiState.isLoginFinish -> onLoginFinish()
			uiState.isExistEmail -> state.showMessage("이미 사용되는 이메일이에요 ${Emoji.invalid.random()}")
			uiState.isNetworkError -> state.showMessage("네트워크 상태를 확인해 주세요 ${Emoji.fail.random()}")
			uiState.isUnknownError -> state.showMessage("알 수 없는 에러가 발생했어요 잠시 후 다시 시도해 주세요 ${Emoji.error.random()}")
		}

		uiState.onMessageShow()
	}
}
