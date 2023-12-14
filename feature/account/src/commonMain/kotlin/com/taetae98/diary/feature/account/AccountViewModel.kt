package com.taetae98.diary.feature.account

import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.account.Credential
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.account.SignInUseCase
import com.taetae98.diary.domain.usecase.account.SignOutUseCase
import com.taetae98.diary.library.viewmodel.ViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class AccountViewModel(
    getAccountUseCase: GetAccountUseCase,
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {
    private val account = getAccountUseCase(Unit)
        .mapLatest { it.getOrNull() ?: Account.Guest }

    val uiState = account.mapLatest {
        when (it) {
            Account.Guest -> AccountUiState.Guest(onSignIn = ::signIn)
            is Account.Member -> AccountUiState.Member(
                uid = it.uid,
                email = it.email,
                onSignOut = ::signOut,
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AccountUiState.Guest(onSignIn = ::signIn)
    )

    private fun signIn(credential: Credential) {
        viewModelScope.launch {
            signInUseCase(credential)
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            signOutUseCase(Unit)
        }
    }
}