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
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.core.resources.Res
import io.github.taetae98coding.diary.core.resources.bottom_button_email_blank
import io.github.taetae98coding.diary.core.resources.bottom_button_password_blank
import io.github.taetae98coding.diary.core.resources.check_password
import io.github.taetae98coding.diary.core.resources.icon.NavigateUpIcon
import io.github.taetae98coding.diary.core.resources.join
import io.github.taetae98coding.diary.core.resources.join_button_invalid_email_message
import io.github.taetae98coding.diary.core.resources.join_button_message
import io.github.taetae98coding.diary.core.resources.join_button_password_different_message
import io.github.taetae98coding.diary.core.resources.join_exist_email_message
import io.github.taetae98coding.diary.core.resources.network_error
import io.github.taetae98coding.diary.core.resources.password
import io.github.taetae98coding.diary.core.resources.unknown_error
import io.github.taetae98coding.diary.feature.account.common.BasePasswordTextField
import io.github.taetae98coding.diary.feature.account.common.BottomBarButton
import io.github.taetae98coding.diary.feature.account.common.BottomBarButtonContent
import io.github.taetae98coding.diary.feature.account.common.EmailTextField
import io.github.taetae98coding.diary.feature.account.join.state.JoinScreenButtonUiState
import io.github.taetae98coding.diary.feature.account.join.state.JoinScreenState
import io.github.taetae98coding.diary.feature.account.join.state.JoinUiState
import org.jetbrains.compose.resources.stringResource

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
                title = { Text(text = stringResource(Res.string.join)) },
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
private fun JoinButtonContent(
    uiState: JoinScreenButtonUiState,
    modifier: Modifier = Modifier,
) {
    BottomBarButtonContent(modifier = modifier) {
        when (uiState) {
            JoinScreenButtonUiState.JoinEnable -> {
                Text(text = stringResource(Res.string.join_button_message))
            }

            JoinScreenButtonUiState.EmailBlank -> {
                Text(text = stringResource(Res.string.bottom_button_email_blank))
            }

            JoinScreenButtonUiState.PasswordBlank -> {
                Text(text = stringResource(Res.string.bottom_button_password_blank))
            }

            JoinScreenButtonUiState.InvalidEmail -> {
                Text(text = stringResource(Res.string.join_button_invalid_email_message))
            }

            JoinScreenButtonUiState.PasswordDifferent -> {
                Text(text = stringResource(Res.string.join_button_password_different_message))
            }

            JoinScreenButtonUiState.Progress -> {
                CircularProgressIndicator(color = LocalContentColor.current)
            }
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
        placeholder = { Text(text = stringResource(Res.string.password)) },
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
        placeholder = { Text(text = stringResource(Res.string.check_password)) },
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
    val existEmailMessage = stringResource(Res.string.join_exist_email_message)
    val networkErrorMessage = stringResource(Res.string.network_error)
    val unknownErrorMessage = stringResource(Res.string.unknown_error)

    LaunchedEffect(
        uiState.isLoginFinish,
        uiState.isExistEmail,
        uiState.isNetworkError,
        uiState.isUnknownError,
    ) {
        if (!uiState.hasMessage) return@LaunchedEffect

        when {
            uiState.isLoginFinish -> onLoginFinish()
            uiState.isExistEmail -> state.showMessage(existEmailMessage)
            uiState.isNetworkError -> state.showMessage(networkErrorMessage)
            uiState.isUnknownError -> state.showMessage(uiState.message)
        }

        uiState.onMessageShow()
    }
}
