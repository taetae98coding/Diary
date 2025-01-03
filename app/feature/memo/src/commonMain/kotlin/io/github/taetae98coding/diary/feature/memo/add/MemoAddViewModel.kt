package io.github.taetae98coding.diary.feature.memo.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.github.taetae98coding.diary.common.exception.ext.isNetworkException
import io.github.taetae98coding.diary.common.exception.memo.MemoTitleBlankException
import io.github.taetae98coding.diary.core.compose.memo.detail.MemoDetailScaffoldUiState
import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty
import io.github.taetae98coding.diary.core.compose.tag.card.TagCardItemUiState
import io.github.taetae98coding.diary.core.compose.tag.card.TagCardUiState
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.navigation.memo.MemoAddDestination
import io.github.taetae98coding.diary.domain.memo.usecase.AddMemoUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagUseCase
import io.github.taetae98coding.diary.library.navigation.LocalDateNavType
import kotlin.reflect.typeOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.android.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
internal class MemoAddViewModel(
	savedStateHandle: SavedStateHandle,
	private val addMemoUseCase: AddMemoUseCase,
	pageTagUseCase: PageTagUseCase,
) : ViewModel() {
	val route = savedStateHandle.toRoute<MemoAddDestination>(
		typeMap = mapOf(typeOf<LocalDate?>() to LocalDateNavType),
	)

	private val _uiState = MutableStateFlow(
		MemoDetailScaffoldUiState.Add(
			add = ::add,
			clearState = ::clearState,
		),
	)
	val uiState = _uiState.asStateFlow()

	private val refreshFlow = MutableStateFlow(0)
	private val tagPageList = refreshFlow.flatMapLatest { pageTagUseCase() }
		.shareIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			replay = 1,
		)
	private val selectedTag = MutableStateFlow(setOfNotNull(savedStateHandle.get<String?>(MemoAddDestination.SELECTED_TAG)))
	private val primaryTag = MutableStateFlow<String?>(null)

	val primaryTagUiState = combine(tagPageList, selectedTag, primaryTag) { listResult, selected, primary ->
		if (listResult.isSuccess) {
			val list = listResult.getOrNull()
				.orEmpty()
				.filter { it.id in selected }
				.map {
					TagCardItemUiState(
						id = it.id,
						title = it.detail.title,
						isSelected = it.id == primary,
						color = it.detail.color,
						select = SkipProperty { primaryTag(it.id) },
						unselect = SkipProperty { deletePrimaryTag() },
					)
				}

			TagCardUiState.State(list)
		} else {
			when (val exception = listResult.exceptionOrNull()) {
				is Exception if(exception.isNetworkException()) -> TagCardUiState.NetworkError(::refresh)
				else -> TagCardUiState.UnknownError(::refresh)
			}
		}
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5_000),
		initialValue = TagCardUiState.Loading,
	)

	val tagUiState = combine(tagPageList, selectedTag) { listResult, selected ->
		if (listResult.isSuccess) {
			val list = listResult.getOrNull()
				.orEmpty()
				.map {
					TagCardItemUiState(
						id = it.id,
						title = it.detail.title,
						isSelected = selected.contains(it.id),
						color = it.detail.color,
						select = SkipProperty { selectTag(it.id) },
						unselect = SkipProperty { unselectTag(it.id) },
					)
				}

			TagCardUiState.State(list)
		} else {
			when (val exception = listResult.exceptionOrNull()) {
				is Exception if(exception.isNetworkException()) -> TagCardUiState.NetworkError(::refresh)
				else -> TagCardUiState.UnknownError(::refresh)
			}
		}
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5_000),
		initialValue = TagCardUiState.Loading,
	)

	private fun clearState() {
		_uiState.update {
			it.copy(
				isAddFinish = false,
				isTitleBlankError = false,
				isUnknownError = false,
			)
		}
	}

	fun add(detail: MemoDetail) {
		if (uiState.value.isAddInProgress) return

		viewModelScope.launch {
			_uiState.update { it.copy(isAddInProgress = true) }
			addMemoUseCase(detail = detail, primaryTag = primaryTag.value, tagIds = selectedTag.value)
				.onSuccess { _uiState.update { it.copy(isAddInProgress = false, isAddFinish = true) } }
				.onFailure(::handleThrowable)
		}
	}

	private fun handleThrowable(throwable: Throwable) {
		when (throwable) {
			is MemoTitleBlankException -> _uiState.update { it.copy(isAddInProgress = false, isTitleBlankError = true) }
			else -> _uiState.update { it.copy(isAddInProgress = false, isUnknownError = true) }
		}
	}

	private fun selectTag(tagId: String) {
		selectedTag.update {
			buildSet {
				addAll(it)
				add(tagId)
			}
		}
	}

	private fun unselectTag(tagId: String) {
		selectedTag.update {
			buildSet {
				addAll(it)
				remove(tagId)
			}
		}
	}

	private fun primaryTag(tagId: String) {
		primaryTag.update { tagId }
	}

	private fun deletePrimaryTag() {
		primaryTag.update { null }
	}

	private fun refresh() {
		viewModelScope.launch {
			refreshFlow.emit(refreshFlow.value + 1)
		}
	}
}
