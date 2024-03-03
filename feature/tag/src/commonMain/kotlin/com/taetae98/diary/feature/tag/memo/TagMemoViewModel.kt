package com.taetae98.diary.feature.tag.memo

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.taetae98.diary.domain.usecase.memo.DeleteMemoUseCase
import com.taetae98.diary.domain.usecase.memo.FinishMemoUseCase
import com.taetae98.diary.domain.usecase.memo.PageMemoByTagIdUseCase
import com.taetae98.diary.domain.usecase.tag.FindTagByIdUseCase
import com.taetae98.diary.library.paging.mapPagingLatest
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.navigation.core.tag.TagMemoEntry
import com.taetae98.diary.ui.memo.compose.MemoUiState
import com.taetae98.diary.ui.memo.compose.SwipeMemoUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class TagMemoViewModel(
    savedStateHandle: SavedStateHandle,
    private val findTagByIdUseCase: FindTagByIdUseCase,
    private val pageMemoByTagIdUseCase: PageMemoByTagIdUseCase,
    private val finishMemoUseCase: FinishMemoUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase,
) : ViewModel() {
    private val tagId = savedStateHandle.getStateFlow<String?>(TagMemoEntry.TAG_ID, null)

    val title = tagId.flatMapLatest { findTagByIdUseCase(it) }
        .mapLatest { it.getOrNull() }
        .mapLatest { it?.title.orEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = "",
        )

    val memoPagingData = tagId.flatMapLatest { pageMemoByTagIdUseCase(it) }
        .mapLatest { it.getOrNull() ?: PagingData.empty() }
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
            finishMemoUseCase(id)
        }
    }

    private fun delete(id: String) {
        viewModelScope.launch {
            deleteMemoUseCase(id)
        }
    }
}