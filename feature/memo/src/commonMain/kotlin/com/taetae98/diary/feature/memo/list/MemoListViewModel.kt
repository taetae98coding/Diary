package com.taetae98.diary.feature.memo.list

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.usecase.memo.DeleteMemoUseCase
import com.taetae98.diary.domain.usecase.memo.PageMemoBySelectTagUseCase
import com.taetae98.diary.domain.usecase.memo.UpdateMemoFinishUseCase
import com.taetae98.diary.domain.usecase.memo.UpsertMemoUseCase
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
    pageMemoBySelectTagUseCase: PageMemoBySelectTagUseCase,
    private val updateMemoFinishUseCase: UpdateMemoFinishUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase,
    private val upsertMemoUseCase: UpsertMemoUseCase,
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

    val memoPagingData = pageMemoBySelectTagUseCase(Unit).mapLatest { it.getOrNull() ?: PagingData.empty() }
        .cachedIn(viewModelScope)
        .mapPagingLatest {
            SwipeMemoUiState(
                memo = MemoUiState(
                    id = it.id,
                    title = it.title,
                    dateRange = it.dateRange,
                ),
                finish = ::finish,
                delete = ::delete,
            )
        }.cachedIn(viewModelScope)

    private fun finish(id: String) {
        viewModelScope.launch {
            val param = UpdateMemoFinishUseCase.Params(
                memoId = id,
                isFinish = true,
            )

            updateMemoFinishUseCase(param).onSuccess {
                val message = MemoListMessage.Finish(cancel = { notFinish(id) })
                messageFlow.emit(message)
            }
        }
    }

    private fun notFinish(id: String) {
        viewModelScope.launch {
            val param = UpdateMemoFinishUseCase.Params(
                memoId = id,
                isFinish = false,
            )

            updateMemoFinishUseCase(param)
        }
    }

    private fun delete(id: String) {
        viewModelScope.launch {
            deleteMemoUseCase(id).onSuccess {
                val message = MemoListMessage.Delete(cancel = { it?.let(::upsert) })
                messageFlow.emit(message)
            }
        }
    }

    private fun upsert(memo: Memo) {
        viewModelScope.launch {
            upsertMemoUseCase(memo)
        }
    }

    private fun messageShow() {
        viewModelScope.launch {
            messageFlow.emit(null)
        }
    }
}