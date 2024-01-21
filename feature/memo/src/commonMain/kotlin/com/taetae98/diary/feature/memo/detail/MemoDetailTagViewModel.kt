package com.taetae98.diary.feature.memo.detail

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.usecase.memo.tag.SwitchMemoTagUseCase
import com.taetae98.diary.domain.usecase.tag.PageTagUseCase
import com.taetae98.diary.feature.memo.tag.TagUiState
import com.taetae98.diary.library.paging.mapPagingLatest
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.navigation.core.memo.MemoDetailEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class MemoDetailTagViewModel(
    private val savedStateHandle: SavedStateHandle,
    pageTagUseCase: PageTagUseCase,
    private val switchMemoTagUseCase: SwitchMemoTagUseCase,
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val pagingData = pageTagUseCase(Unit).mapLatest { it.getOrNull() ?: PagingData.empty() }
        .cachedIn(viewModelScope)

    val tagUiState = pagingData.mapPagingLatest {
        TagUiState(
            id = it.id,
            title = it.title,
            onClick = ::switchTagSelected,
        )
    }.cachedIn(viewModelScope)

    private fun switchTagSelected(tagId: String) {
        val memoId = savedStateHandle.getStateFlow<String?>(MemoDetailEntry.ID, null).value
        val memoTag = MemoTag(
            memoId = memoId ?: return,
            tagId = tagId,
        )

        viewModelScope.launch {
            switchMemoTagUseCase(memoTag)
        }
    }
}