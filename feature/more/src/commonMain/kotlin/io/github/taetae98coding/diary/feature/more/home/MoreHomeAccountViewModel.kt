package io.github.taetae98coding.diary.feature.more.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.credentials.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class MoreHomeAccountViewModel(
    getAccountUseCase: GetAccountUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val isInLogoutProgress = MutableStateFlow(false)
    private val account = getAccountUseCase()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            replay = 1,
        )

    val uiState: StateFlow<MoreHomeAccountUiState> = combine(getAccountUseCase(), isInLogoutProgress) { accountResult, loggingOut ->
        if (loggingOut) {
            MoreHomeAccountUiState.Loading
        } else {
            accountResult.fold(
                onSuccess = { account ->
                    when (account) {
                        is Account.Guest -> MoreHomeAccountUiState.NotLogin

                        is Account.User -> MoreHomeAccountUiState.Login(
                            email = account.email,
                            profileImage = account.profileImage,
                        )
                    }
                },
                onFailure = { MoreHomeAccountUiState.NotLogin },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MoreHomeAccountUiState.Loading,
    )

    fun logout() {
        viewModelScope.launch {
            isInLogoutProgress.value = true
            logoutUseCase()
            isInLogoutProgress.value = false
        }
    }
}
