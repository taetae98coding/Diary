package io.github.taetae98coding.diary.feature.memo.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.github.taetae98coding.diary.common.exception.memo.MemoTitleBlankException
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.navigation.memo.MemoAddDestination
import io.github.taetae98coding.diary.domain.memo.usecase.AddMemoUseCase
import io.github.taetae98coding.diary.feature.memo.detail.MemoDetailScreenUiState
import io.github.taetae98coding.diary.library.navigation.LocalDateNavType
import kotlin.reflect.typeOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class MemoAddViewModel(
    savedStateHandle: SavedStateHandle,
    private val addMemoUseCase: AddMemoUseCase,
) : ViewModel() {
    val route = savedStateHandle.toRoute<MemoAddDestination>(
        typeMap = mapOf(typeOf<LocalDate?>() to LocalDateNavType),
    )

    private val _uiState = MutableStateFlow(MemoDetailScreenUiState(onMessageShow = ::clearMessage))
    val uiState = _uiState.asStateFlow()

    private fun clearMessage() {
        _uiState.update {
            it.copy(
                isAdd = false,
                isTitleBlankError = false,
                isUnknownError = false,
            )
        }
    }

    fun add(detail: MemoDetail) {
        if (uiState.value.isProgress) return

        viewModelScope.launch {
            _uiState.update { it.copy(isProgress = true) }
            addMemoUseCase(detail)
                .onSuccess { _uiState.update { it.copy(isProgress = false, isAdd = true) } }
                .onFailure { handleThrowable(it) }
        }
    }

    private fun handleThrowable(throwable: Throwable) {
        when (throwable) {
            is MemoTitleBlankException -> _uiState.update { it.copy(isProgress = false, isTitleBlankError = true) }
            else -> _uiState.update { it.copy(isProgress = false, isUnknownError = true) }
        }
    }
}
