package io.github.taetae98coding.diary.feature.memo.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.common.exception.ext.isNetworkException
import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty
import io.github.taetae98coding.diary.core.compose.tag.card.TagCardItemUiState
import io.github.taetae98coding.diary.core.compose.tag.card.TagCardUiState
import io.github.taetae98coding.diary.core.navigation.memo.MemoDetailDestination
import io.github.taetae98coding.diary.domain.memo.usecase.DeleteMemoPrimaryTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.FindMemoTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.SelectMemoTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.UnselectMemoTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.UpdateMemoPrimaryTagUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
internal class MemoDetailTagViewModel(
	savedStateHandle: SavedStateHandle,
	findMemoTagUseCase: FindMemoTagUseCase,
	private val updateMemoPrimaryTagUseCase: UpdateMemoPrimaryTagUseCase,
	private val deleteMemoPrimaryTagUseCase: DeleteMemoPrimaryTagUseCase,
	private val selectMemoTagUseCase: SelectMemoTagUseCase,
	private val unselectMemoTagUseCase: UnselectMemoTagUseCase,
) : ViewModel() {
	private val memoId = savedStateHandle.getStateFlow<String?>(MemoDetailDestination.MEMO_ID, null)

	private val refreshFlow = MutableStateFlow(0)
	private val memoTagList = refreshFlow.flatMapLatest { memoId.flatMapLatest { findMemoTagUseCase(it) } }
		.shareIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			replay = 1,
		)

	val primaryTagUiState = memoTagList.mapLatest { result ->
		if (result.isSuccess) {
			val list = result.getOrNull()
				.orEmpty()
				.filter { it.isSelected }
				.map {
					TagCardItemUiState(
						id = it.tag.id,
						title = it.tag.detail.title,
						isSelected = it.isPrimary,
						color = it.tag.detail.color,
						select = SkipProperty { primaryTag(it.tag.id) },
						unselect = SkipProperty { deletePrimaryTag() },
					)
				}

			TagCardUiState.State(list)
		} else {
			when (val exception = result.exceptionOrNull()) {
				is Exception if(exception.isNetworkException()) -> TagCardUiState.NetworkError(::refresh)
				else -> TagCardUiState.UnknownError(::refresh)
			}
		}
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5_000),
		initialValue = TagCardUiState.Loading,
	)

	val tagUiState = memoTagList.mapLatest { result ->
		if (result.isSuccess) {
			val list = result.getOrNull()
				.orEmpty()
				.map {
					TagCardItemUiState(
						id = it.tag.id,
						title = it.tag.detail.title,
						isSelected = it.isSelected,
						color = it.tag.detail.color,
						select = SkipProperty { selectTag(it.tag.id) },
						unselect = SkipProperty { unselectTag(it.tag.id) },
					)
				}

			TagCardUiState.State(list)
		} else {
			when (val exception = result.exceptionOrNull()) {
				is Exception if(exception.isNetworkException()) -> TagCardUiState.NetworkError(::refresh)
				else -> TagCardUiState.UnknownError(::refresh)
			}
		}
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5_000),
		initialValue = TagCardUiState.Loading,
	)

	private fun primaryTag(tagId: String) {
		viewModelScope.launch {
			updateMemoPrimaryTagUseCase(memoId.value, tagId)
		}
	}

	private fun deletePrimaryTag() {
		viewModelScope.launch {
			deleteMemoPrimaryTagUseCase(memoId.value)
		}
	}

	private fun selectTag(tagId: String) {
		viewModelScope.launch {
			selectMemoTagUseCase(memoId.value, tagId)
		}
	}

	private fun unselectTag(tagId: String) {
		viewModelScope.launch {
			unselectMemoTagUseCase(memoId.value, tagId)
		}
	}

	private fun refresh() {
		viewModelScope.launch {
			refreshFlow.emit(refreshFlow.value + 1)
		}
	}
}
