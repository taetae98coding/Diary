package io.github.taetae98coding.diary.feature.buddy.memo.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.github.taetae98coding.diary.common.exception.ext.isNetworkException
import io.github.taetae98coding.diary.core.compose.memo.detail.MemoDetailScaffoldUiState
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.navigation.buddy.BuddyGroupMemoDetailDestination
import io.github.taetae98coding.diary.domain.memo.usecase.DeleteMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.FindMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.FinishMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.RestartMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.UpdateMemoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
internal class BuddyGroupMemoDetailViewModel(
	savedStateHandle: SavedStateHandle,
	findMemoUseCase: FindMemoUseCase,
	private val finishMemoUseCase: FinishMemoUseCase,
	private val restartMemoUseCase: RestartMemoUseCase,
	private val deleteMemoUseCase: DeleteMemoUseCase,
	private val updateMemoUseCase: UpdateMemoUseCase,
) : ViewModel() {
	private val route = savedStateHandle.toRoute<BuddyGroupMemoDetailDestination>()

	private val _uiState = MutableStateFlow(MemoDetailScaffoldUiState(onMessageShow = ::clearMessage))
	val uiState = _uiState.asStateFlow()

	val memo = findMemoUseCase(route.memoId)
		.mapLatest { it.getOrNull() }
		.filterNotNull()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = null,
		)

	val detail = memo.mapLatest { it?.detail }
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = null,
		)

	fun onFinishChange(isFinish: Boolean) {
		viewModelScope.launch {
			if (isFinish) {
				finishMemoUseCase(route.memoId)
					.onFailure { handleThrowable(it) }
			} else {
				restartMemoUseCase(route.memoId)
					.onFailure { handleThrowable(it) }
			}
		}
	}

	fun delete() {
		viewModelScope.launch {
			deleteMemoUseCase(route.memoId)
				.onSuccess { _uiState.update { it.copy(isDelete = true) } }
				.onFailure { handleThrowable(it) }
		}
	}

	fun update(detail: MemoDetail) {
		viewModelScope.launch {
			updateMemoUseCase(route.memoId, detail)
				.onSuccess { _uiState.update { it.copy(isUpdate = true) } }
				.onFailure { handleThrowable(it) }
		}
	}

	private fun clearMessage() {
		_uiState.update {
			it.copy(
				isDelete = false,
				isUpdate = false,
				isNetworkError = false,
				isUnknownError = false,
			)
		}
	}

	private fun handleThrowable(throwable: Throwable) {
		_uiState.update {
			when (throwable) {
				is Exception if throwable.isNetworkException() -> it.copy(isNetworkError = true)
				else -> it.copy(isUnknownError = true)
			}
		}
	}
}
