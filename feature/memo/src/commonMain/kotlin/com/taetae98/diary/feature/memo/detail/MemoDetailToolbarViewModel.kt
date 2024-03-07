package com.taetae98.diary.feature.memo.detail

import com.taetae98.diary.domain.usecase.memo.DeleteMemoUseCase
import com.taetae98.diary.domain.usecase.memo.FindMemoByIdUseCase
import com.taetae98.diary.domain.usecase.memo.UpdateMemoFinishUseCase
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.navigation.core.memo.MemoDetailEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class MemoDetailToolbarViewModel(
    savedStateHandle: SavedStateHandle,
    private val findMemoByIdUseCase: FindMemoByIdUseCase,
    private val updateMemoFinishUseCase: UpdateMemoFinishUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase,
) : ViewModel() {
    private val id = savedStateHandle.getStateFlow(
        key = MemoDetailEntry.ID,
        initialValue = "",
    )

    private val memo = id.flatMapLatest { findMemoByIdUseCase(it) }
        .mapLatest { it.getOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    val uiState = memo.mapLatest {
        MemoDetailToolbarUiState.Detail(
            isFinished = it?.isFinished ?: false,
            onFinish = ::finish,
            onDelete = ::delete,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MemoDetailToolbarUiState.Detail(
            isFinished = false,
            onFinish = ::finish,
            onDelete = ::delete,
        ),
    )

    private fun finish() {
        val memo = memo.value ?: return

        viewModelScope.launch {
            val param = UpdateMemoFinishUseCase.Params(
                memoId = id.value,
                isFinish = !memo.isFinished
            )

            updateMemoFinishUseCase(param)
        }
    }

    private fun delete() {
        viewModelScope.launch {
            deleteMemoUseCase(id.value)
        }
    }
}