package com.taetae98.diary.feature.memo.add

import com.taetae98.diary.domain.entity.account.memo.Memo
import com.taetae98.diary.domain.entity.account.memo.MemoState
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.memo.UpsertMemoUseCase
import com.taetae98.diary.feature.memo.detail.MemoDetailMessage
import com.taetae98.diary.feature.memo.detail.MemoDetailToolbarUiState
import com.taetae98.diary.feature.memo.detail.MemoDetailUiState
import com.taetae98.diary.library.uuid.getUuid
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.ui.compose.text.TextFieldUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class MemoAddViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getAccountUseCase: GetAccountUseCase,
    private val upsertMemoUseCase: UpsertMemoUseCase,
) : ViewModel() {
    private val _message = MutableStateFlow<MemoDetailMessage?>(null)
    private val _toolbarUiState = MutableStateFlow(MemoDetailToolbarUiState.Add)
    private val _title = savedStateHandle.getStateFlow(
        key = TITLE,
        initialValue = "",
    )

    val uiState = _message.mapLatest {
        MemoDetailUiState.Add(
            onAdd = ::add,
            message = it,
            onMessageShown = ::messageShown,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MemoDetailUiState.Add(
            onAdd = ::add,
            message = null,
            onMessageShown = ::messageShown,
        )
    )

    val toolbarUiState = _toolbarUiState.asStateFlow()

    val titleUiState = _title.mapLatest { title ->
        TextFieldUiState(
            value = title,
            onValueChange = ::setTitle,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = TextFieldUiState(
            value = _title.value,
            onValueChange = ::setTitle,
        )
    )

    private fun setTitle(title: String) {
        savedStateHandle[TITLE] = title
    }

    private fun add() {
        viewModelScope.launch {
            val ownerId = getAccountUseCase(Unit).first().getOrNull()?.uid
            val memo = Memo(
                id = getUuid(),
                title = _title.value,
                ownerId = ownerId,
                state = MemoState.INCOMPLETE,
            )

            upsertMemoUseCase(memo).onSuccess {
                showAddMessage()
                clearInput()
            }
        }
    }

    private fun clearInput() {
        savedStateHandle[TITLE] = ""
    }

    private suspend fun showAddMessage() {
        _message.emit(MemoDetailMessage.Add)
    }

    private suspend fun clearMessage() {
        _message.emit(null)
    }

    private fun messageShown() {
        viewModelScope.launch {
            clearMessage()
        }
    }

    companion object {
        private const val TITLE = "title"
    }
}