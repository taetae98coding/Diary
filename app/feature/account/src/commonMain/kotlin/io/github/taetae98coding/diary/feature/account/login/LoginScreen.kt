package io.github.taetae98coding.diary.feature.account.login

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
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.core.resources.Res
import io.github.taetae98coding.diary.core.resources.account_not_found_error
import io.github.taetae98coding.diary.core.resources.bottom_button_email_blank
import io.github.taetae98coding.diary.core.resources.bottom_button_password_blank
import io.github.taetae98coding.diary.core.resources.check_password
import io.github.taetae98coding.diary.core.resources.icon.NavigateUpIcon
import io.github.taetae98coding.diary.core.resources.login
import io.github.taetae98coding.diary.core.resources.login_button_message
import io.github.taetae98coding.diary.core.resources.network_error
import io.github.taetae98coding.diary.core.resources.unknown_error
import io.github.taetae98coding.diary.feature.account.common.BasePasswordTextField
import io.github.taetae98coding.diary.feature.account.common.BottomBarButton
import io.github.taetae98coding.diary.feature.account.common.BottomBarButtonContent
import io.github.taetae98coding.diary.feature.account.common.EmailTextField
import io.github.taetae98coding.diary.feature.account.login.state.LoginScreenButtonUiState
import io.github.taetae98coding.diary.feature.account.login.state.LoginScreenState
import io.github.taetae98coding.diary.feature.account.login.state.LoginUiState
import org.jetbrains.compose.resources.stringResource

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
                title = { Text(text = stringResource(Res.string.login)) },
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
    ) {
        Content(
            state = state,
            onLogin = onLogin,
            modifier = Modifier.padding(it)
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
    val accountNotFoundErrorMessage = stringResource(Res.string.account_not_found_error)
    val networkErrorMessage = stringResource(Res.string.network_error)
    val unknownErrorMessage = stringResource(Res.string.unknown_error)

    LaunchedEffect(
        uiState.isLoginFinish,
        uiState.isAccountNotFound,
        uiState.isNetworkError,
        uiState.isUnknownError,
    ) {
        if (!uiState.hasMessage) return@LaunchedEffect

        when {
            uiState.isLoginFinish -> onLoginFinish()
            uiState.isAccountNotFound -> state.showMessage(accountNotFoundErrorMessage)
            uiState.isNetworkError -> state.showMessage(networkErrorMessage)
            uiState.isUnknownError -> state.showMessage(unknownErrorMessage)
        }

        uiState.onMessageShow()
    }
}

@Composable
private fun LoginButtonContent(
    uiState: LoginScreenButtonUiState,
    modifier: Modifier = Modifier,
) {
    BottomBarButtonContent(modifier = modifier) {
        when (uiState) {
            LoginScreenButtonUiState.LoginEnable -> {
                Text(text = stringResource(Res.string.login_button_message))
            }

            LoginScreenButtonUiState.EmailBlank -> {
                Text(text = stringResource(Res.string.bottom_button_email_blank))
            }

            LoginScreenButtonUiState.PasswordBlank -> {
                Text(text = stringResource(Res.string.bottom_button_password_blank))
            }

            LoginScreenButtonUiState.Progress -> {
                CircularProgressIndicator(color = LocalContentColor.current)
            }
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
            placeholder = { Text(text = stringResource(Res.string.check_password)) },
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
