package com.taetae98.diary.feature.finished.memo

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.taetae98.diary.domain.usecase.memo.PageFinishMemoUseCase
import com.taetae98.diary.library.paging.mapPagingLatest
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.ui.memo.compose.MemoUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class FinishedMemoViewModel(
    pageFinishMemoUseCase: PageFinishMemoUseCase,
) : ViewModel() {
    private val pagingData = pageFinishMemoUseCase(Unit)
        .mapLatest { it.getOrNull() ?: PagingData.empty() }
        .cachedIn(viewModelScope)

    val memoPaging = pagingData.mapPagingLatest {
        MemoUiState(
            id = it.id,
            title = it.title,
            dateRange = it.dateRange,
        )
    }
}
