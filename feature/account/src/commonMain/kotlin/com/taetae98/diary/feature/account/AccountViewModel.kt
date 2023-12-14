package com.taetae98.diary.feature.account

import com.taetae98.diary.domain.usecase.account.LoginUseCase
import com.taetae98.diary.library.viewmodel.ViewModel
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class AccountViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    val uiState = AccountUiState.Guest(
        onLogin = ::login,
    )

    private fun login(idToken: String) {
        viewModelScope.launch {
            loginUseCase(idToken)
        }
    }
}