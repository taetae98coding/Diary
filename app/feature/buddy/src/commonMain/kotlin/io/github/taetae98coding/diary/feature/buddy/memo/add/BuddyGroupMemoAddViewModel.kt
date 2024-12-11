package io.github.taetae98coding.diary.feature.buddy.memo.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.github.taetae98coding.diary.common.exception.ext.isNetworkException
import io.github.taetae98coding.diary.common.exception.memo.MemoTitleBlankException
import io.github.taetae98coding.diary.core.compose.memo.detail.MemoDetailScaffoldUiState
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.navigation.buddy.BuddyGroupMemoAddDestination
import io.github.taetae98coding.diary.domain.buddy.usecase.AddBuddyGroupMemoUseCase
import io.github.taetae98coding.diary.library.navigation.LocalDateNavType
import kotlin.reflect.typeOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class BuddyGroupMemoAddViewModel(
	savedStateHandle: SavedStateHandle,
	private val addBuddyGroupMemoUseCase: AddBuddyGroupMemoUseCase,
) : ViewModel() {
	val route = savedStateHandle.toRoute<BuddyGroupMemoAddDestination>(
		typeMap = mapOf(typeOf<LocalDate?>() to LocalDateNavType),
	)

	private val _uiState = MutableStateFlow(
		MemoDetailScaffoldUiState.Add(
			add = ::add,
			clearState = ::clearState,
		),
	)
	val uiState = _uiState.asStateFlow()

	fun add(detail: MemoDetail) {
		if (uiState.value.isAddInProgress) return

		viewModelScope.launch {
			_uiState.update { it.copy(isAddInProgress = true) }
			addBuddyGroupMemoUseCase(
				groupId = route.groupId,
				detail = detail,
				primaryTag = null,
				tagIds = emptySet(),
			).onSuccess {
				_uiState.update { it.copy(isAddInProgress = false, isAddFinish = true) }
			}.onFailure(::handleThrowable)
		}
	}

	private fun handleThrowable(throwable: Throwable) {
		when (throwable) {
			is MemoTitleBlankException -> _uiState.update { it.copy(isAddInProgress = false, isTitleBlankError = true) }
			is Exception if throwable.isNetworkException() -> _uiState.update { it.copy(isAddInProgress = false, isNetworkError = true) }
			else -> _uiState.update { it.copy(isAddInProgress = false, isUnknownError = true) }
		}
	}

	private fun clearState() {
		_uiState.update {
			it.copy(
				isAddFinish = false,
				isTitleBlankError = false,
				isNetworkError = false,
				isUnknownError = false,
			)
		}
	}
}
