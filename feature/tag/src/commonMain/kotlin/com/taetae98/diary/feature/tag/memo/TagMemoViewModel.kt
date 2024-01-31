package com.taetae98.diary.feature.tag.memo

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.taetae98.diary.domain.usecase.memo.PageMemoByTagIdUseCase
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.navigation.core.tag.TagMemoEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@Factory
internal class TagMemoViewModel(
    savedStateHandle: SavedStateHandle,
    private val pageMemoByTagIdUseCase: PageMemoByTagIdUseCase,
) : ViewModel() {
    private val tagId = savedStateHandle.getStateFlow<String?>(TagMemoEntry.TAG_ID, null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val memoPagingData = tagId.flatMapLatest { pageMemoByTagIdUseCase(it) }
        .mapLatest { it.getOrNull() ?: PagingData.empty() }
        .cachedIn(viewModelScope)
}