package com.taetae98.diary.feature.memo.add

import com.taetae98.diary.domain.entity.account.memo.Memo
import com.taetae98.diary.domain.entity.account.memo.MemoState
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.memo.UpsertMemoUseCase
import com.taetae98.diary.feature.memo.detail.MemoDetailMessage
import com.taetae98.diary.feature.memo.detail.MemoDetailUiState
import com.taetae98.diary.library.uuid.getUuid
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.ui.compose.entity.EntityDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class MemoAddViewModel(
    private val getAccountUseCase: GetAccountUseCase,
    private val upsertMemoUseCase: UpsertMemoUseCase,
) : ViewModel() {
    private val _message = MutableStateFlow<MemoDetailMessage?>(null)
    private val _title = MutableStateFlow("")

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

    val detailUiState = _title.mapLatest { title ->
        EntityDetailUiState(
            title = title,
            setTitle = ::setTitle,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = EntityDetailUiState(
            title = _title.value,
            setTitle = ::setTitle,
        )
    )

    private fun setTitle(title: String) {
        viewModelScope.launch {
            _title.emit(title)
        }
    }

    private fun add() {
        viewModelScope.launch {
            val ownerId = getAccountUseCase(Unit).first().getOrNull()?.uid
            val memo = Memo(
                id = getUuid(),
                title = _title.value,
                ownerId = ownerId,
                state = MemoState.NONE,
            )

            upsertMemoUseCase(memo).onSuccess {
                showAddMessage()
                clearInput()
            }
        }
    }

    private suspend fun clearInput() {
        _title.emit("")
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
}