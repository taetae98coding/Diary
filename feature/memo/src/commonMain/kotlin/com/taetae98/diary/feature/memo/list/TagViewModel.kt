package com.taetae98.diary.feature.memo.list

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import app.cash.paging.filter
import com.taetae98.diary.domain.usecase.tag.FindTagInMemoUseCase
import com.taetae98.diary.domain.usecase.tag.PageTagUseCase
import com.taetae98.diary.feature.memo.tag.TagUiState
import com.taetae98.diary.library.kotlin.ext.mapCollectionLatest
import com.taetae98.diary.library.paging.mapPaging
import com.taetae98.diary.library.viewmodel.ViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class TagViewModel(
    findTagInMemoUseCase: FindTagInMemoUseCase,
    pageTagUseCase: PageTagUseCase,
) : ViewModel() {
    private val tagInMemoList = findTagInMemoUseCase(Unit).mapLatest { it.getOrNull() }
        .mapLatest { it.orEmpty() }

    private val tagPagingData = pageTagUseCase(Unit).mapLatest { it.getOrNull() ?: PagingData.empty() }
        .cachedIn(viewModelScope)

    val selectTagList = tagInMemoList.mapCollectionLatest {
        TagUiState(
            id = it.id,
            isSelected = true,
            title = it.title,
            onClick = ::unselectTag,
        )
    }.mapLatest {
        it.toImmutableList()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = persistentListOf(),
    )

    val notSelectTagPagingData = combine(tagPagingData, tagInMemoList) { pagingData, tagInMemoList ->
        val tagIdSet = tagInMemoList.map { it.id }.toSet()

        pagingData.filter {
            it.id !in tagIdSet
        }
    }.mapPaging {
        TagUiState(
            id = it.id,
            isSelected = false,
            title = it.title,
            onClick = ::selectTag
        )
    }.cachedIn(viewModelScope)

    private fun unselectTag(id: String) {

    }

    private fun selectTag(id: String) {

    }
}