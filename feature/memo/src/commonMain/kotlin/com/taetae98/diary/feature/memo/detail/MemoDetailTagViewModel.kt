package com.taetae98.diary.feature.memo.detail

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import app.cash.paging.map
import com.taetae98.diary.domain.entity.memo.MemoId
import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.usecase.memo.tag.DeleteMemoTagUseCase
import com.taetae98.diary.domain.usecase.memo.tag.FindMemoTagByMemoIdUseCase
import com.taetae98.diary.domain.usecase.memo.tag.UpsertMemoTagUseCase
import com.taetae98.diary.domain.usecase.tag.PageTagUseCase
import com.taetae98.diary.feature.memo.tag.TagUiState
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.navigation.core.memo.MemoDetailEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class MemoDetailTagViewModel(
    savedStateHandle: SavedStateHandle,
    pageTagUseCase: PageTagUseCase,
    findMemoTagByMemoIdUseCase: FindMemoTagByMemoIdUseCase,
    private val upsertMemoTagUseCase: UpsertMemoTagUseCase,
    private val deleteMemoTagUseCase: DeleteMemoTagUseCase,
) : ViewModel() {
    private val memoId = savedStateHandle.getStateFlow(
        key = MemoDetailEntry.ID,
        initialValue = "",
    )

    private val memoTag = memoId.flatMapLatest { findMemoTagByMemoIdUseCase(MemoId(it)) }
        .mapLatest { it.getOrNull().orEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList(),
        )

    private val pagingData = pageTagUseCase(Unit).mapLatest { it.getOrNull() ?: PagingData.empty() }
        .cachedIn(viewModelScope)

    val tagUiState = combine(memoTag, pagingData) { memoTag, pagingData ->
        pagingData.map { tag ->
            TagUiState(
                id = tag.id,
                isSelected = memoTag.any { it.tagId == tag.id },
                title = tag.title,
                select = ::upsert,
                unselect = ::delete,
            )
        }
    }.cachedIn(viewModelScope)

    private fun upsert(tagId: String) {
        val memoTag = MemoTag(
            memoId = memoId.value,
            tagId = tagId,
        )

        viewModelScope.launch {
            upsertMemoTagUseCase(memoTag)
        }
    }

    private fun delete(tagId: String) {
        val memoTag = MemoTag(
            memoId = memoId.value,
            tagId = tagId,
        )

        viewModelScope.launch {
            deleteMemoTagUseCase(memoTag)
        }
    }
}
