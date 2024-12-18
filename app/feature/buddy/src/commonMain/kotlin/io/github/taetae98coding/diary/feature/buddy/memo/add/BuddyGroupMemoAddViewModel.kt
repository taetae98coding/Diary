package io.github.taetae98coding.diary.feature.buddy.memo.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.github.taetae98coding.diary.common.exception.memo.MemoTitleBlankException
import io.github.taetae98coding.diary.core.compose.memo.detail.MemoDetailScaffoldUiState
import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty
import io.github.taetae98coding.diary.core.compose.tag.card.TagCardItemUiState
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.navigation.buddy.BuddyGroupMemoAddDestination
import io.github.taetae98coding.diary.core.navigation.memo.MemoAddDestination
import io.github.taetae98coding.diary.domain.buddy.usecase.AddBuddyGroupMemoUseCase
import io.github.taetae98coding.diary.library.navigation.LocalDateNavType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.android.annotation.KoinViewModel
import kotlin.reflect.typeOf

@KoinViewModel
internal class BuddyGroupMemoAddViewModel(
	savedStateHandle: SavedStateHandle,
	private val addBuddyGroupMemoUseCase: AddBuddyGroupMemoUseCase,
) : ViewModel() {
	val route = savedStateHandle.toRoute<BuddyGroupMemoAddDestination>(
		typeMap = mapOf(typeOf<LocalDate?>() to LocalDateNavType),
	)

	private val _uiState = MutableStateFlow(MemoDetailScaffoldUiState(onMessageShow = ::clearMessage))
	val uiState = _uiState.asStateFlow()

	private val selectedTag = MutableStateFlow(setOfNotNull(savedStateHandle.get<String?>(MemoAddDestination.SELECTED_TAG)))
	private val primaryTag = MutableStateFlow<String?>(null)

	private val tagPageList = MutableStateFlow(emptyList<Tag>())

	val primaryTagList =
		combine(tagPageList, selectedTag, primaryTag) { list, selected, primary ->
			list
				?.filter { selected.contains(it.id) }
				?.map {
					TagCardItemUiState(
						id = it.id,
						title = it.detail.title,
						isSelected = it.id == primary,
						color = it.detail.color,
						select = SkipProperty { primaryTag(it.id) },
						unselect = SkipProperty { deletePrimaryTag() },
					)
				}
		}.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = null,
		)

	val tagList =
		combine(tagPageList, selectedTag) { list, selected ->
			list?.map {
				TagCardItemUiState(
					id = it.id,
					title = it.detail.title,
					isSelected = selected.contains(it.id),
					color = it.detail.color,
					select = SkipProperty { selectTag(it.id) },
					unselect = SkipProperty { unselectTag(it.id) },
				)
			}
		}.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = null,
		)

	fun add(detail: MemoDetail) {
		if (uiState.value.isProgress) return

		viewModelScope.launch {
			_uiState.update { it.copy(isProgress = true) }
			addBuddyGroupMemoUseCase(
				groupId = route.groupId,
				detail = detail,
				primaryTag = primaryTag.value,
				tagIds = selectedTag.value,
			).onSuccess {
				_uiState.update { it.copy(isProgress = false, isAdd = true) }
			}.onFailure(::handleThrowable)
		}
	}

	private fun handleThrowable(throwable: Throwable) {
		when (throwable) {
			is MemoTitleBlankException -> _uiState.update { it.copy(isProgress = false, isTitleBlankError = true) }
			else -> _uiState.update { it.copy(isProgress = false, isUnknownError = true) }
		}
	}

	private fun clearMessage() {
		_uiState.update {
			it.copy(
				isAdd = false,
				isTitleBlankError = false,
				isUnknownError = false,
			)
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
}
