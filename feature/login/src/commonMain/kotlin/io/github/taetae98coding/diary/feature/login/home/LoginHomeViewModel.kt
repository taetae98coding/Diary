package io.github.taetae98coding.diary.feature.login.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentials
import io.github.taetae98coding.diary.domain.credentials.usecase.LoginByGoogleAuthorizationCodeUseCase
import io.github.taetae98coding.diary.domain.credentials.usecase.LoginByGoogleIdTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class LoginHomeViewModel(
    private val loginByGoogleAuthorizationCodeUseCase: LoginByGoogleAuthorizationCodeUseCase,
    private val loginByGoogleIdTokenUseCase: LoginByGoogleIdTokenUseCase,
) : ViewModel() {
    private val _isInProgress = MutableStateFlow(false)
    val isInProgress: StateFlow<Boolean> = _isInProgress.asStateFlow()

    private val _effect = MutableStateFlow<LoginEffect>(LoginEffect.None)
    val effect: StateFlow<LoginEffect> = _effect.asStateFlow()

    fun login(googleCredentials: GoogleCredentials) {
        if (_isInProgress.value) return

        viewModelScope.launch {
            _isInProgress.value = true
            when (googleCredentials) {
                is GoogleCredentials.AuthorizationCode -> {
                    loginByGoogleAuthorizationCodeUseCase(googleCredentials.clientId, googleCredentials.code, googleCredentials.redirectUri)
                        .onSuccess { _effect.value = LoginEffect.Finish }
                        .onFailure(::handleLoginError)
                }

                is GoogleCredentials.IdToken -> {
                    loginByGoogleIdTokenUseCase(googleCredentials.idToken)
                        .onSuccess { _effect.value = LoginEffect.Finish }
                        .onFailure(::handleLoginError)
                }
            }
            _isInProgress.value = false
        }
    }

    fun consumeEffect() {
        _effect.value = LoginEffect.None
    }

    fun handleLoginError(throwable: Throwable) {
        throwable.printStackTrace()
        when (throwable) {
            else -> _effect.value = LoginEffect.UnknownError
        }
    }
}
