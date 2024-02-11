package com.taetae98.diary.feature.tag.memo

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.taetae98.diary.domain.usecase.memo.CompleteMemoUseCase
import com.taetae98.diary.domain.usecase.memo.DeleteMemoUseCase
import com.taetae98.diary.domain.usecase.memo.PageMemoByTagIdUseCase
import com.taetae98.diary.library.paging.mapPagingLatest
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.navigation.core.tag.TagMemoEntry
import com.taetae98.diary.ui.memo.compose.MemoUiState
import com.taetae98.diary.ui.memo.compose.SwipeMemoUiState
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class TagMemoViewModel(
    savedStateHandle: SavedStateHandle,
    private val pageMemoByTagIdUseCase: PageMemoByTagIdUseCase,
    private val completeMemoUseCase: CompleteMemoUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase,
) : ViewModel() {
    val tagId = savedStateHandle.getStateFlow<String?>(TagMemoEntry.TAG_ID, null)

    val memoPagingData = tagId.flatMapLatest { pageMemoByTagIdUseCase(it) }
        .mapLatest { it.getOrNull() ?: PagingData.empty() }
        .mapPagingLatest {
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