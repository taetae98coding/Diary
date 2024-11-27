package io.github.taetae98coding.diary.feature.tag.memo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty
import io.github.taetae98coding.diary.core.navigation.tag.TagDetailDestination
import io.github.taetae98coding.diary.domain.memo.usecase.DeleteMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.FinishMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.RestartMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.RestoreMemoUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagMemoUseCase
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
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

	private val _uiState =
		MutableStateFlow(
			TagMemoScreenUiState(
				restartTag = ::restart,
				restoreTag = ::restore,
				onMessageShow = ::clearMessage,
			),
		)
	val uiState = _uiState.asStateFlow()

	val memoList = tagId
		.flatMapLatest { pageTagMemoUseCase(it) }
		.mapLatest { it.getOrNull() }
		.mapCollectionLatest {
			val start = it.detail.start
			val endInclusive = it.detail.endInclusive

			MemoListItemUiState(
				id = it.id,
				title = it.detail.title,
				dateRange = if (start != null && endInclusive != null) {
					start..endInclusive
				} else {
					null
				},
				finish = SkipProperty { finish(it.id) },
				delete = SkipProperty { delete(it.id) },
			)
		}.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = null,
		)

	private fun finish(memoId: String) {
		viewModelScope.launch {
			finishMemoUseCase(memoId)
				.onSuccess { _uiState.update { it.copy(finishTagId = memoId) } }
				.onFailure { _uiState.update { it.copy(isUnknownError = true) } }
		}
	}

	private fun delete(memoId: String) {
		viewModelScope.launch {
			deleteMemoUseCase(memoId)
				.onSuccess { _uiState.update { it.copy(deleteTagId = memoId) } }
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

	private fun clearMessage() {
		_uiState.update {
			it.copy(
				finishTagId = null,
				deleteTagId = null,
				isUnknownError = false,
			)
		}
	}

	fun fetch(tagId: String?) {
		savedStateHandle[TagDetailDestination.TAG_ID] = tagId
	}
}
