package io.github.taetae98coding.diary.feature.more.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.credential.usecase.LogoutUseCase
import io.github.taetae98coding.diary.feature.more.account.state.MoreAccountUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
internal class MoreAccountViewModel(getAccountUseCase: GetAccountUseCase, private val logoutUseCase: LogoutUseCase) : ViewModel() {
	private val isProgress = MutableStateFlow(false)
	private val account =
		getAccountUseCase()
			.mapLatest { it.getOrNull() }
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = null,
			)

	val uiState =
		combine(isProgress, account) { isProgress, account ->
			if (isProgress) {
				MoreAccountUiState.Loading
			} else if (account == null) {
				MoreAccountUiState.Loading
			} else {
				when (account) {
					is Account.Guest -> MoreAccountUiState.Guest
					is Account.Member ->
						MoreAccountUiState.Member(
							email = account.email,
							logout = ::logout,
						)
				}
			}
		}.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = MoreAccountUiState.Loading,
		)

	private fun logout() {
		viewModelScope.launch {
			isProgress.emit(true)
			logoutUseCase()
			isProgress.emit(false)
		}
	}
}
