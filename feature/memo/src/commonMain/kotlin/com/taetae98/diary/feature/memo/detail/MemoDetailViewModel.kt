package com.taetae98.diary.feature.memo.detail

import com.taetae98.diary.domain.entity.account.memo.Memo
import com.taetae98.diary.domain.entity.account.memo.MemoState
import com.taetae98.diary.domain.usecase.memo.CompleteMemoUseCase
import com.taetae98.diary.domain.usecase.memo.DeleteMemoUseCase
import com.taetae98.diary.domain.usecase.memo.FindMemoUseCase
import com.taetae98.diary.domain.usecase.memo.IncompleteMemoUseCase
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.navigation.core.memo.MemoDetailEntry
import com.taetae98.diary.ui.compose.text.TextFieldUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class MemoDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val findMemoUseCase: FindMemoUseCase,
    private val completeMemoUseCase: CompleteMemoUseCase,
    private val incompleteMemoUseCase: IncompleteMemoUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase,
) : ViewModel() {
    private val id = savedStateHandle.getStateFlow<String?>(
        key = MemoDetailEntry.ID,
        initialValue = null
    )

    private val memo = id.flatMapLatest { findMemoUseCase(it) }
        .mapLatest(Result<Memo?>::getOrNull)
        .onEach(::onMemoChanged)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    private val title = savedStateHandle.getStateFlow(
        key = TITLE,
        initialValue = ""
    )

    val uiState = MutableStateFlow(
        MemoDetailUiState.Detail(
            message = null,
            onMessageShown = {},
        )
    )

    val toolbarUiState = memo.mapLatest {
        MemoDetailToolbarUiState.Detail(
            isComplete = it?.state == MemoState.COMPLETE,
            onComplete = ::toggleComplete,
            onDelete = ::delete,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MemoDetailToolbarUiState.Detail(
            isComplete = memo.value?.state == MemoState.COMPLETE,
            onComplete = ::toggleComplete,
            onDelete = ::delete,
        )
    )

    val titleUiState = title.mapLatest {
        TextFieldUiState(
            value = it,
            onValueChange = ::setTitle,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = TextFieldUiState(
            value = title.value,
            onValueChange = ::setTitle,
        )
    )

    private fun onMemoChanged(memo: Memo?) {
        setTitle(memo?.title.orEmpty())
    }

    private fun setTitle(title: String) {
        savedStateHandle[TITLE] = title
    }

    private fun toggleComplete() {
        val memo = memo.value ?: return

        viewModelScope.launch {
            when (memo.state) {
                MemoState.INCOMPLETE -> completeMemoUseCase(memo.id)
                MemoState.COMPLETE -> incompleteMemoUseCase(memo.id)
                else -> Unit
            }
        }
    }

    private fun delete() {
        viewModelScope.launch {
            id.value?.let { deleteMemoUseCase(it) }
        }
    }

    companion object {
        private const val TITLE = "title"
    }
}