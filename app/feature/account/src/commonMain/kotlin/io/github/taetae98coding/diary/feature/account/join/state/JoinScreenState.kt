package io.github.taetae98coding.diary.feature.account.join.state

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import io.github.taetae98coding.diary.library.kotlin.regex.email
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class JoinScreenState(
	val coroutineScope: CoroutineScope,
) {
	private var messageJob: Job? = null
	val hostState: SnackbarHostState = SnackbarHostState()

	var email by mutableStateOf("")
		private set

	var isPasswordVisible by mutableStateOf(false)
		private set

	var password by mutableStateOf("")
		private set

	var isCheckPasswordVisible by mutableStateOf(false)

	var checkPassword by mutableStateOf("")
		private set

	val buttonState by derivedStateOf {
		if (email.isBlank()) {
			JoinScreenButtonUiState.EmailBlank
		} else if (password.isBlank()) {
			JoinScreenButtonUiState.PasswordBlank
		} else if (!email.contains(Regex.email())) {
			JoinScreenButtonUiState.InvalidEmail
		} else if (password != checkPassword) {
			JoinScreenButtonUiState.PasswordDifferent
		} else {
			JoinScreenButtonUiState.JoinEnable
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

	fun onCheckPasswordVisibleChange(value: Boolean) {
		isCheckPasswordVisible = value
	}

	fun onCheckPasswordChange(value: String) {
		checkPassword = value
	}

	fun showMessage(message: String) {
		messageJob?.cancel()
		messageJob = coroutineScope.launch { hostState.showSnackbar(message) }
	}

	companion object {
		fun saver(coroutineScope: CoroutineScope): Saver<JoinScreenState, Any> =
			listSaver(
				save = { listOf(it.email, it.password, it.checkPassword) },
				restore = {
					JoinScreenState(coroutineScope).apply {
						email = it[0]
						password = it[1]
						checkPassword = it[2]
					}
				},
			)
	}
}
