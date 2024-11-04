package io.github.taetae98coding.diary.feature.account.login.state

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class LoginScreenState(
    private val coroutineScope: CoroutineScope,
) {
    private var messageJob: Job? = null
    val hostState: SnackbarHostState = SnackbarHostState()

    var email by mutableStateOf("")
        private set
    var isPasswordVisible by mutableStateOf(false)
        private set
    var password by mutableStateOf("")
        private set

    val buttonState by derivedStateOf {
        if (email.isBlank()) {
            LoginScreenButtonUiState.EmailBlank
        } else if (password.isBlank()) {
            LoginScreenButtonUiState.PasswordBlank
        } else {
            LoginScreenButtonUiState.LoginEnable
        }
    }

    fun onEmailChange(value: String) {
        email = value
    }

    fun onPasswordVisibleChange(value: Boolean) {
        isPasswordVisible = value
    }

    fun onPasswordChange(value: String) {
        password = value
    }

    fun showMessage(message: String) {
        messageJob?.cancel()
        messageJob = coroutineScope.launch { hostState.showSnackbar(message) }
    }

    companion object {
        fun saver(
            coroutineScope: CoroutineScope,
        ): Saver<LoginScreenState, Any> {
            return listSaver(
                save = { listOf(it.email, it.password) },
                restore = {
                    LoginScreenState(coroutineScope = coroutineScope).apply {
                        email = it[0]
                        password = it[1]
                    }
                },
            )
        }
    }
}
