package com.taetae98.diary.feature.memo.list

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.taetae98.diary.domain.usecase.memo.BeginMemoUseCase
import com.taetae98.diary.domain.usecase.memo.DeleteMemoUseCase
import com.taetae98.diary.domain.usecase.memo.FinishMemoUseCase
import com.taetae98.diary.domain.usecase.memo.PageMemoUseCase
import com.taetae98.diary.library.paging.mapPagingLatest
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.ui.memo.compose.MemoUiState
import com.taetae98.diary.ui.memo.compose.SwipeMemoUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class MemoListViewModel(
    pageMemoUseCase: PageMemoUseCase,
    private val finishMemoUseCase: FinishMemoUseCase,
    private val beginMemoUseCase: BeginMemoUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase,
) : ViewModel() {
    private val messageFlow = MutableStateFlow<MemoListMessage?>(null)

    val messageUiState = messageFlow.map {
        MemoListMessageUiState(
            message = it,
            messageShow = ::messageShow,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MemoListMessageUiState(
            message = messageFlow.value,
            messageShow = ::messageShow,
        ),
    )

    val memoPagingData = pageMemoUseCase(Unit).mapLatest { it.getOrNull() ?: PagingData.empty() }
        .cachedIn(viewModelScope)
        .mapPagingLatest {
            SwipeMemoUiState(
                memo = MemoUiState(
                    id = it.id,
                    title = it.title,
                ),
                finish = ::finish,
                delete = ::delete,
            )
        }.cachedIn(viewModelScope)

    private fun finish(id: String) {
        viewModelScope.launch {
            finishMemoUseCase(id).onSuccess {
                val message = MemoListMessage.Finish(cancel = { begin(id) })
                messageFlow.emit(message)
            }
        }
    }

    private fun begin(id: String) {
        viewModelScope.launch {
            beginMemoUseCase(id)
        }
    }

    private fun delete(id: String) {
        viewModelScope.launch {
            deleteMemoUseCase(id).onSuccess {
                messageFlow.emit(MemoListMessage.Delete)
            }
        }
    }

    private fun messageShow() {
        viewModelScope.launch {
            messageFlow.emit(null)
        }
    }
}