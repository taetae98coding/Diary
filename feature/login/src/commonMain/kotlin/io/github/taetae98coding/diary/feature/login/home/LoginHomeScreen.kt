package io.github.taetae98coding.diary.feature.login.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.compose.core.snackbar.showImmediate
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsNotExistException
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsNotSupportException
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsUserCancelException
import io.github.taetae98coding.diary.feature.login.google.rememberGoogleLoginState
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun LoginHomeScreen(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginHomeViewModel = koinViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val googleCredentialsManager = rememberGoogleCredentialsManagerCompat()
    val googleLoginState = rememberGoogleLoginState {
        coroutineScope.launch {
            try {
                viewModel.login(googleCredentialsManager.get())
            } catch (_: GoogleCredentialsNotSupportException) {
                // Do nothing
            } catch (_: GoogleCredentialsUserCancelException) {
                // Do nothing
            } catch (_: GoogleCredentialsNotExistException) {
                // Do nothing
            } catch (throwable: Throwable) {
                viewModel.handleLoginError(throwable)
            }
        }
    }
    val state = rememberLoginHomeScaffoldState()
    val isInProgress by viewModel.isInProgress.collectAsStateWithLifecycle()

    LoginHomeScaffold(
        modifier = modifier,
        state = state,
        isInProgressProvider = { isInProgress },
        onNavigateUp = navigateUp,
        onGoogleLogin = {
            coroutineScope.launch {
                try {
                    viewModel.login(googleCredentialsManager.get())
                } catch (_: GoogleCredentialsNotSupportException) {
                    // Do nothing
                } catch (_: GoogleCredentialsUserCancelException) {
                    // Do nothing
                } catch (_: GoogleCredentialsNotExistException) {
                    googleLoginState.login()
                } catch (throwable: Throwable) {
                    viewModel.handleLoginError(throwable)
                }
            }
        },
    )

    LoginEffectHandler(
        viewModel = viewModel,
        state = state,
        navigateUp = navigateUp,
    )
}

@Composable
private fun LoginEffectHandler(
    viewModel: LoginHomeViewModel,
    state: LoginHomeScaffoldState,
    navigateUp: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val effect by viewModel.effect.collectAsStateWithLifecycle()

    LaunchedEffect(effect) {
        when (effect) {
            LoginEffect.Finish -> {
                navigateUp()
                viewModel.consumeEffect()
            }

            LoginEffect.UnknownError -> {
                coroutineScope.launch { state.hostState.showImmediate("네트워크 상태를 확인하세요") }
                viewModel.consumeEffect()
            }

            LoginEffect.None -> Unit
        }
    }
}
