package com.taetae98.diary.feature.account

import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.account.Credential
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.account.SignInUseCase
import com.taetae98.diary.domain.usecase.account.SignOutUseCase
import com.taetae98.diary.library.viewmodel.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    @OptIn(ExperimentalCoroutinesApi::class)
    private val account = getAccountUseCase(Unit)
        .mapLatest(Result<Account>::getOrNull)
        .mapLatest { it ?: Account.Guest }

    @OptIn(ExperimentalCoroutinesApi::class)
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
        started = SharingStarted.Eagerly,
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