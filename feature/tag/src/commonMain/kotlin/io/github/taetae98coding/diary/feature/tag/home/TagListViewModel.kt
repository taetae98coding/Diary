@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.feature.tag.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class TagListViewModel(pageTagUseCase: PageTagUseCase) : ViewModel() {
    val pagingData: Flow<PagingData<Tag>> = pageTagUseCase()
        .mapLatest { it.getOrDefault(PagingData.empty()) }
        .cachedIn(viewModelScope)
}
