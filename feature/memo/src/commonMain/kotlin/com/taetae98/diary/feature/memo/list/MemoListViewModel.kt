package com.taetae98.diary.feature.memo.list

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.taetae98.diary.domain.usecase.memo.CompleteMemoUseCase
import com.taetae98.diary.domain.usecase.memo.DeleteMemoUseCase
import com.taetae98.diary.domain.usecase.memo.PageMemoUseCase
import com.taetae98.diary.library.paging.mapPagingLatest
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.ui.memo.compose.MemoUiState
import com.taetae98.diary.ui.memo.compose.SwipeMemoUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class MemoListViewModel(
    pageMemoUseCase: PageMemoUseCase,
    private val completeMemoUseCase: CompleteMemoUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase,
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val pagingData = pageMemoUseCase(Unit).mapLatest { it.getOrNull() ?: PagingData.empty() }
        .cachedIn(viewModelScope)

    val memoPagingData = pagingData.mapPagingLatest {
        SwipeMemoUiState(
            memo = MemoUiState(
                id = it.id,
                title = it.title,
            ),
            complete = ::complete,
            delete = ::delete,
        )
    }.cachedIn(viewModelScope)

    private fun complete(id: String) {
        viewModelScope.launch {
            completeMemoUseCase(id)
        }
    }

    private fun delete(id: String) {
        viewModelScope.launch {
            deleteMemoUseCase(id)
        }
    }
}