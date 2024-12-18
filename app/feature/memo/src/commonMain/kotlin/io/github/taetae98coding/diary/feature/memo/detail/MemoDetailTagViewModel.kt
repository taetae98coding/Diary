package io.github.taetae98coding.diary.feature.memo.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty
import io.github.taetae98coding.diary.core.compose.tag.card.TagCardItemUiState
import io.github.taetae98coding.diary.core.navigation.memo.MemoDetailDestination
import io.github.taetae98coding.diary.domain.memo.usecase.DeleteMemoPrimaryTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.FindMemoTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.SelectMemoTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.UnselectMemoTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.UpdateMemoPrimaryTagUseCase
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
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

	private val memoTagList =
		memoId
			.flatMapLatest { findMemoTagUseCase(it) }
			.mapLatest { it.getOrNull() }
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = null,
			)

	val primaryTagList =
		memoTagList
			.mapLatest { list -> list?.filter { it.isSelected } }
			.mapCollectionLatest {
				TagCardItemUiState(
					id = it.tag.id,
					title = it.tag.detail.title,
					isSelected = it.isPrimary,
					color = it.tag.detail.color,
					select = SkipProperty { primaryTag(it.tag.id) },
					unselect = SkipProperty { deletePrimaryTag() },
				)
			}.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = null,
			)

	val tagList =
		memoTagList
			.mapCollectionLatest {
				TagCardItemUiState(
					id = it.tag.id,
					title = it.tag.detail.title,
					isSelected = it.isSelected,
					color = it.tag.detail.color,
					select = SkipProperty { selectTag(it.tag.id) },
					unselect = SkipProperty { unselectTag(it.tag.id) },
				)
			}.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = null,
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
}
