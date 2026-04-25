package io.github.taetae98coding.diary.feature.memo.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import io.github.taetae98coding.diary.core.model.FilterPresence
import io.github.taetae98coding.diary.core.model.memo.ListMemoFilterOption
import io.github.taetae98coding.diary.domain.memo.usecase.GetListMemoFilterOptionUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.GetListMemoFilterTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.SelectListMemoFilterTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.SetDatePresenceFilterUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.SetTagPresenceFilterUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.UnselectListMemoFilterTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagUseCase
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class MemoListFilterViewModel(
    pageTagUseCase: PageTagUseCase,
    getListMemoFilterTagUseCase: GetListMemoFilterTagUseCase,
    getListMemoFilterOptionUseCase: GetListMemoFilterOptionUseCase,
    private val selectListMemoFilterTagUseCase: SelectListMemoFilterTagUseCase,
    private val unselectListMemoFilterTagUseCase: UnselectListMemoFilterTagUseCase,
    private val setTagPresenceFilterUseCase: SetTagPresenceFilterUseCase,
    private val setDatePresenceFilterUseCase: SetDatePresenceFilterUseCase,
) : ViewModel() {
    private val tagPagingData: Flow<PagingData<io.github.taetae98coding.diary.core.model.tag.Tag>> = pageTagUseCase()
        .mapLatest { it.getOrDefault(PagingData.empty()) }
        .cachedIn(viewModelScope)

    private val selectedTagIds = getListMemoFilterTagUseCase()
        .mapLatest { it.getOrDefault(emptySet()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet(),
        )

    val filterTagPagingData: Flow<PagingData<MemoListFilterTagUiState>> = combine(tagPagingData, selectedTagIds) { pagingData, selectedIds ->
        pagingData.map { tag ->
            MemoListFilterTagUiState(
                id = tag.id,
                title = tag.detail.title,
                color = tag.detail.color,
                isSelected = tag.id in selectedIds,
            )
        }
    }.cachedIn(viewModelScope)

    val filterOption = getListMemoFilterOptionUseCase()
        .mapLatest { it.getOrDefault(ListMemoFilterOption()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ListMemoFilterOption(),
        )

    fun select(tagId: Uuid) {
        viewModelScope.launch {
            selectListMemoFilterTagUseCase(tagId)
        }
    }

    fun unselect(tagId: Uuid) {
        viewModelScope.launch {
            unselectListMemoFilterTagUseCase(tagId)
        }
    }

    fun setTagPresence(tagPresence: FilterPresence) {
        viewModelScope.launch {
            setTagPresenceFilterUseCase(tagPresence)
        }
    }

    fun setDatePresence(datePresence: FilterPresence) {
        viewModelScope.launch {
            setDatePresenceFilterUseCase(datePresence)
        }
    }
}
