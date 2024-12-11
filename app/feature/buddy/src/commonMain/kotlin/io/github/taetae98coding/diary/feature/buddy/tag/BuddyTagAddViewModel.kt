package io.github.taetae98coding.diary.feature.buddy.tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.common.exception.ext.isNetworkException
import io.github.taetae98coding.diary.common.exception.tag.TagTitleBlankException
import io.github.taetae98coding.diary.core.compose.tag.detail.TagDetailScaffoldUiState
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class BuddyTagAddViewModel : ViewModel() {
	private val _uiState = MutableStateFlow(
		TagDetailScaffoldUiState.Add(
			add = ::add,
			clearState = ::clearState,
		),
	)
	val uiState = _uiState.asStateFlow()

	private fun add(detail: TagDetail) {
		if (uiState.value.isAddInProgress) {
			return
		}

		viewModelScope.launch {
			_uiState.update { it.copy(isAddInProgress = true) }
//            addTagUseCase(detail)
//                .onSuccess { _uiState.update { it.copy(isAddInProgress = false, isAddFinish = true) } }
//                .onFailure(::handleThrowable)
			delay(2000L)
			_uiState.update { it.copy(isAddInProgress = false) }
		}
	}

	private fun handleThrowable(throwable: Throwable) {
		when (throwable) {
			is TagTitleBlankException -> _uiState.update { it.copy(isAddInProgress = false, isTitleBlankError = true) }
			is Exception if (throwable.isNetworkException()) -> _uiState.update { it.copy(isAddInProgress = false, isNetworkError = true) }
			else -> _uiState.update { it.copy(isAddInProgress = false, isUnknownError = true) }
		}
	}

	private fun clearState() {
		_uiState.update {
			it.copy(
				isAddFinish = false,
				isTitleBlankError = false,
				isUnknownError = false,
			)
		}
	}
}
