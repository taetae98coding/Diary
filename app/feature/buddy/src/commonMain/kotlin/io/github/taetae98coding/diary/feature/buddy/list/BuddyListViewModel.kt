package io.github.taetae98coding.diary.feature.buddy.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.common.exception.ext.isNetworkException
import io.github.taetae98coding.diary.domain.buddy.usecase.PageBuddyGroupUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
internal class BuddyListViewModel(
	pageBuddyGroupUseCase: PageBuddyGroupUseCase,
) : ViewModel() {
	private val retryFlow = MutableStateFlow(0)

	val listUiState = retryFlow.flatMapLatest { pageBuddyGroupUseCase() }
		.mapLatest { result ->
			if (result.isSuccess) {
				BuddyListUiState.State(result.getOrThrow())
			} else {
				when (val exception = result.exceptionOrNull()) {
					is Exception if (exception.isNetworkException()) -> BuddyListUiState.NetworkError(::retry)
					else -> BuddyListUiState.UnknownError(::retry)
				}
			}
		}
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = BuddyListUiState.Loading,
		)

	private fun retry() {
		viewModelScope.launch {
			retryFlow.emit(retryFlow.value + 1)
		}
	}
}
