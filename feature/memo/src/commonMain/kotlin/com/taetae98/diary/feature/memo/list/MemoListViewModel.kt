package com.taetae98.diary.feature.memo.list

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
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
    private val deleteMemoUseCase: DeleteMemoUseCase,
) : ViewModel() {
    private val message = MutableStateFlow<MemoListMessage?>(null)

    val messageUiState = message.map {
        MemoListMessageUiState(
            message = it,
            messageShow = ::messageShow,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MemoListMessageUiState(
            message = message.value,
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
                message.emit(MemoListMessage.Finish)
            }
        }
    }

    private fun delete(id: String) {
        viewModelScope.launch {
            deleteMemoUseCase(id).onSuccess {
                message.emit(MemoListMessage.Delete)
            }
        }
    }

    private fun messageShow() {
        viewModelScope.launch {
            message.emit(null)
        }
    }
}