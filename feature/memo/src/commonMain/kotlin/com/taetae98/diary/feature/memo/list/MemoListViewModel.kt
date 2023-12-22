package com.taetae98.diary.feature.memo.list

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.taetae98.diary.domain.usecase.memo.DeleteMemoUseCase
import com.taetae98.diary.domain.usecase.memo.PageMemoUseCase
import com.taetae98.diary.library.paging.mapPagingLatest
import com.taetae98.diary.library.viewmodel.ViewModel
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class MemoListViewModel(
    pageMemoUseCase: PageMemoUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase,
) : ViewModel() {
    private val pagingData = pageMemoUseCase(Unit).mapLatest { it.getOrNull() ?: PagingData.empty() }
        .cachedIn(viewModelScope)

    val memoPagingData = pagingData.mapPagingLatest {
        MemoListUiState(
            id = it.id,
            title = it.title,
            delete = ::delete,
        )
    }

    private fun delete(id: String) {
        viewModelScope.launch {
            deleteMemoUseCase(id)
        }
    }
}