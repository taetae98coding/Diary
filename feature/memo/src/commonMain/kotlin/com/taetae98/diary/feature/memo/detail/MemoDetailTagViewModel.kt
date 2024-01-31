package com.taetae98.diary.feature.memo.detail

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import app.cash.paging.map
import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.usecase.memo.tag.FindMemoTagByMemoIdUseCase
import com.taetae98.diary.domain.usecase.memo.tag.SwitchMemoTagUseCase
import com.taetae98.diary.domain.usecase.tag.PageTagUseCase
import com.taetae98.diary.feature.memo.tag.TagUiState
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.navigation.core.memo.MemoDetailEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class MemoDetailTagViewModel(
    savedStateHandle: SavedStateHandle,
    pageTagUseCase: PageTagUseCase,
    findMemoTagByMemoIdUseCase: FindMemoTagByMemoIdUseCase,
    private val switchMemoTagUseCase: SwitchMemoTagUseCase,
) : ViewModel() {
    private val memoId = savedStateHandle.getStateFlow<String?>(MemoDetailEntry.ID, null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val memoTag = memoId.flatMapLatest { findMemoTagByMemoIdUseCase(it) }
        .mapLatest { it.getOrNull().orEmpty() }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val pagingData = pageTagUseCase(Unit).mapLatest { it.getOrNull() ?: PagingData.empty() }
        .cachedIn(viewModelScope)

    val tagUiState = combine(memoTag, pagingData) { memoTag, pagingData ->
        pagingData.map { tag ->
            TagUiState(
                id = tag.id,
                isSelected = memoTag.any { it.tagId == tag.id },
                title = tag.title,
                onClick = ::switchTagSelected,
            )
        }
    }

    private fun switchTagSelected(tagId: String) {
        val memoTag = MemoTag(
            memoId = memoId.value ?: return,
            tagId = tagId,
        )

        viewModelScope.launch {
            switchMemoTagUseCase(memoTag)
        }
    }
}