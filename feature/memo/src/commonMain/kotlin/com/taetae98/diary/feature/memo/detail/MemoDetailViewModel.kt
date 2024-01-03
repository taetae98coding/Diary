package com.taetae98.diary.feature.memo.detail

import com.taetae98.diary.domain.entity.account.memo.Memo
import com.taetae98.diary.domain.entity.account.memo.MemoState
import com.taetae98.diary.domain.exception.TitleEmptyException
import com.taetae98.diary.domain.usecase.memo.CompleteMemoUseCase
import com.taetae98.diary.domain.usecase.memo.DeleteMemoUseCase
import com.taetae98.diary.domain.usecase.memo.FindMemoUseCase
import com.taetae98.diary.domain.usecase.memo.IncompleteMemoUseCase
import com.taetae98.diary.domain.usecase.memo.UpsertMemoUseCase
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.navigation.core.memo.MemoDetailEntry
import com.taetae98.diary.ui.compose.text.TextFieldUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private val upsertMemoUseCase: UpsertMemoUseCase,
) : ViewModel() {
    private val id = savedStateHandle.getStateFlow<String?>(
        key = MemoDetailEntry.ID,
        initialValue = null
    )
    private val _message = MutableStateFlow<MemoDetailMessage?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
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

    val uiState = _message.mapLatest {
        MemoDetailUiState.Detail(
            onUpdate = ::upsert,
            message = it,
            onMessageShown = ::messageShown,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MemoDetailUiState.Detail(
            onUpdate = ::upsert,
            message = _message.value,
            onMessageShown = ::messageShown,
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
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
        val id = id.value ?: return

        viewModelScope.launch {
            deleteMemoUseCase(id).onSuccess {
                _message.emit(MemoDetailMessage.Delete)
            }
        }
    }

    private fun upsert() {
        val memo = Memo(
            id = id.value ?: return,
            title = title.value,
            ownerId = memo.value?.ownerId,
            state = memo.value?.state ?: return
        )

        viewModelScope.launch {
            upsertMemoUseCase(memo).onSuccess {
                _message.emit(MemoDetailMessage.Update)
            }.onFailure {
                handleThrowable(it)
            }
        }
    }

    private suspend fun handleThrowable(throwable: Throwable) {
        when (throwable) {
            is TitleEmptyException -> _message.emit(MemoDetailMessage.TitleEmpty)
        }
    }

    private fun messageShown() {
        viewModelScope.launch {
            _message.emit(null)
        }
    }

    companion object {
        private const val TITLE = "title"
    }
}