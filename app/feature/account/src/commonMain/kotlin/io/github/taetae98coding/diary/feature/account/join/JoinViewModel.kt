package io.github.taetae98coding.diary.feature.account.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.common.exception.NetworkException
import io.github.taetae98coding.diary.common.exception.account.ExistEmailException
import io.github.taetae98coding.diary.domain.account.usecase.JoinUseCase
import io.github.taetae98coding.diary.domain.account.usecase.LoginUseCase
import io.github.taetae98coding.diary.feature.account.join.state.JoinUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class JoinViewModel(
    private val joinUseCase: JoinUseCase,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(JoinUiState(onMessageShow = ::clearMessage))
    val uiState = _uiState.asStateFlow()

    fun join(email: String, password: String) {
        if (uiState.value.isProgress) return

        viewModelScope.launch {
            _uiState.update { it.copy(isProgress = true) }
            joinUseCase(email, password)
                .onSuccess { login(email, password) }
                .onFailure { handleThrowable(it) }
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            loginUseCase(email, password)
            _uiState.update { it.copy(isProgress = false, isLoginFinish = true) }
        }
    }

    private fun handleThrowable(throwable: Throwable) {
        when (throwable) {
            is ExistEmailException -> _uiState.update { it.copy(isProgress = false, isExistEmail = true) }
            is NetworkException -> _uiState.update { it.copy(isProgress = false, isNetworkError = true) }
            else -> _uiState.update { it.copy(isProgress = false, isUnknownError = true, message = it.toString()) }
        }
    }

    private fun clearMessage() {
        _uiState.update {
            it.copy(
                isLoginFinish = false,
                isExistEmail = false,
                isNetworkError = false,
                isUnknownError = false,
            )
        }
    }
}
