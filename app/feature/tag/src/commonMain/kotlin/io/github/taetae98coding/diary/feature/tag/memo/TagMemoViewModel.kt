package io.github.taetae98coding.diary.feature.tag.memo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.common.exception.ext.isNetworkException
import io.github.taetae98coding.diary.core.compose.memo.list.MemoListItemUiState
import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty
import io.github.taetae98coding.diary.core.compose.tag.memo.TagMemoListUiState
import io.github.taetae98coding.diary.core.compose.tag.memo.TagMemoScaffoldUiState
import io.github.taetae98coding.diary.core.navigation.tag.TagDetailDestination
import io.github.taetae98coding.diary.domain.memo.usecase.DeleteMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.FinishMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.RestartMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.RestoreMemoUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagMemoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
internal class TagMemoViewModel(
	private val savedStateHandle: SavedStateHandle,
	private val pageTagMemoUseCase: PageTagMemoUseCase,
	private val finishMemoUseCase: FinishMemoUseCase,
	private val deleteMemoUseCase: DeleteMemoUseCase,
	private val restartMemoUseCase: RestartMemoUseCase,
	private val restoreMemoUseCase: RestoreMemoUseCase,
) : ViewModel() {
	private val tagId = savedStateHandle.getStateFlow<String?>(TagDetailDestination.TAG_ID, null)

	private val _uiState = MutableStateFlow(TagMemoScaffoldUiState(clearState = ::clearState))
	val uiState = _uiState.asStateFlow()

	private val refreshFlow = MutableStateFlow(0)
	val memoList = refreshFlow.flatMapLatest {
		tagId.flatMapLatest { pageTagMemoUseCase(it) }
	}.mapLatest { result ->
		if (result.isSuccess) {
			val list = result.getOrThrow()
				.map {
					MemoListItemUiState(
						id = it.id,
						title = it.detail.title,
						dateRange = it.detail.dateRange,
						finish = SkipProperty { finish(it.id) },
						delete = SkipProperty { delete(it.id) },
					)
				}
			TagMemoListUiState.State(list)
		} else {
			when (val throwable = result.exceptionOrNull()) {
				is Exception if throwable.isNetworkException() -> TagMemoListUiState.NetworkError(::refresh)
				else -> TagMemoListUiState.UnknownError(::refresh)
			}
		}
	}
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = TagMemoListUiState.Loading,
		)

	private fun finish(memoId: String) {
		viewModelScope.launch {
			finishMemoUseCase(memoId)
				.onSuccess {
					_uiState.update {
						it.copy(
							isFinish = true,
							restartTag = { restart(memoId) },
						)
					}
				}
				.onFailure { _uiState.update { it.copy(isUnknownError = true) } }
		}
	}

	private fun delete(memoId: String) {
		viewModelScope.launch {
			deleteMemoUseCase(memoId)
				.onSuccess {
					_uiState.update {
						it.copy(
							isDelete = true,
							restoreTag = { restore(memoId) },
						)
					}
				}
				.onFailure { _uiState.update { it.copy(isUnknownError = true) } }
		}
	}

	private fun restart(id: String) {
		viewModelScope.launch {
			restartMemoUseCase(id)
		}
	}

	private fun restore(id: String) {
		viewModelScope.launch {
			restoreMemoUseCase(id)
		}
	}

	private fun clearState() {
		_uiState.update {
			it.copy(
				isFinish = false,
				isDelete = false,
				isUnknownError = false,
				restartTag = {},
				restoreTag = {},
			)
		}
	}

	private fun refresh() {
		viewModelScope.launch {
			refreshFlow.value++
		}
	}

	fun fetch(tagId: String?) {
		savedStateHandle[TagDetailDestination.TAG_ID] = tagId
	}
}
