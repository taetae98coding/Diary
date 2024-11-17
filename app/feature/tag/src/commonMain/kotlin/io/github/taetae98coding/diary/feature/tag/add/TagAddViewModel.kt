package io.github.taetae98coding.diary.feature.tag.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.common.exception.tag.TagTitleBlankException
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.domain.tag.usecase.AddTagUseCase
import io.github.taetae98coding.diary.feature.tag.detail.TagDetailScreenUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class TagAddViewModel(
    private val addTagUseCase: AddTagUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TagDetailScreenUiState(onMessageShow = ::clearMessage))
    val uiState = _uiState.asStateFlow()

    fun add(detail: TagDetail) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProgress = true) }
            addTagUseCase(detail)
                .onSuccess { _uiState.update { it.copy(isProgress = false, isAdd = true) } }
                .onFailure { handleThrowable(it) }
        }
    }

    private fun handleThrowable(throwable: Throwable) {
        when (throwable) {
            is TagTitleBlankException -> _uiState.update { it.copy(isProgress = false, isTitleBlankError = true) }
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
}
