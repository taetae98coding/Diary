package io.github.taetae98coding.diary.feature.account.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.common.exception.NetworkException
import io.github.taetae98coding.diary.common.exception.account.AccountNotFoundException
import io.github.taetae98coding.diary.domain.credential.usecase.LoginUseCase
import io.github.taetae98coding.diary.feature.account.login.state.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState(onMessageShow = ::clearMessage))
    val uiState = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (uiState.value.isProgress) return

        viewModelScope.launch {
            _uiState.update { it.copy(isProgress = true) }
            loginUseCase(email, password)
                .onSuccess { _uiState.update { it.copy(isProgress = false, isLoginFinish = true) } }
                .onFailure { handleThrowable(it) }
        }
    }

    private fun handleThrowable(throwable: Throwable) {
        when (throwable) {
            is AccountNotFoundException -> _uiState.update { it.copy(isProgress = false, isAccountNotFound = true) }
            is NetworkException -> _uiState.update { it.copy(isProgress = false, isNetworkError = true) }
            else -> _uiState.update { it.copy(isProgress = false, isUnknownError = true) }
        }
    }

    private fun clearMessage() {
        _uiState.update {
            it.copy(
                isLoginFinish = false,
                isAccountNotFound = false,
                isNetworkError = false,
                isUnknownError = false,
            )
        }
    }
}
