package com.taetae98.diary.feature.tag.list

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.taetae98.diary.domain.usecase.tag.PageTagUseCase
import com.taetae98.diary.library.paging.mapPagingLatest
import com.taetae98.diary.library.viewmodel.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@Factory
internal class TagListViewModel(
    pageTagUseCase: PageTagUseCase,
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val pagingData= pageTagUseCase(Unit).mapLatest { it.getOrNull() ?: PagingData.empty() }
        .cachedIn(viewModelScope)

    val tagPagingData = pagingData.mapPagingLatest {
        TagUiState(
            id = it.id,
            title = it.title,
        )
    }.cachedIn(viewModelScope)
}