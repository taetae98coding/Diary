package com.taetae98.diary.feature.tag.memo

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.taetae98.diary.domain.entity.memo.MemoId
import com.taetae98.diary.domain.entity.tag.TagId
import com.taetae98.diary.domain.usecase.memo.DeleteMemoUseCase
import com.taetae98.diary.domain.usecase.memo.PageMemoByTagIdUseCase
import com.taetae98.diary.domain.usecase.memo.UpdateMemoFinishUseCase
import com.taetae98.diary.library.paging.mapPagingLatest
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.navigation.core.tag.TagMemoEntry
import com.taetae98.diary.ui.memo.compose.MemoUiState
import com.taetae98.diary.ui.memo.compose.SwipeMemoUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class TagMemoPagingViewModel(
    savedStateHandle: SavedStateHandle,
    private val pageMemoByTagIdUseCase: PageMemoByTagIdUseCase,
    private val updateMemoFinishUseCase: UpdateMemoFinishUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase,
) : ViewModel() {
    private val tagId = savedStateHandle.getStateFlow<String>(
        key = TagMemoEntry.TAG_ID,
        initialValue = "",
    )

    val memoPagingData = tagId.flatMapLatest { pageMemoByTagIdUseCase(TagId(it)) }
        .mapLatest { it.getOrNull() ?: PagingData.empty() }
        .mapPagingLatest {
            SwipeMemoUiState(
                memo = MemoUiState(
                    id = it.id,
                    title = it.title,
                    dateRange = it.dateRange,
                ),
                finish = ::finish,
                delete = ::delete,
            )
        }.cachedIn(viewModelScope)

    private fun finish(id: String) {
        viewModelScope.launch {
            val params = UpdateMemoFinishUseCase.Params(
                memoId = id,
                isFinish = true,
            )

            updateMemoFinishUseCase(params)
        }
    }

    private fun delete(id: String) {
        viewModelScope.launch {
            deleteMemoUseCase(MemoId(id))
        }
    }
}