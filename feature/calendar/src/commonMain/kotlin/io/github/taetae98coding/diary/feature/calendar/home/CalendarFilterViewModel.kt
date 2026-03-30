package io.github.taetae98coding.diary.feature.calendar.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import io.github.taetae98coding.diary.domain.memo.usecase.GetCalendarMemoFilterTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.SelectCalendarMemoFilterTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.UnselectCalendarMemoFilterTagUseCase
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
internal class CalendarFilterViewModel(
    pageTagUseCase: PageTagUseCase,
    getCalendarMemoFilterTagUseCase: GetCalendarMemoFilterTagUseCase,
    private val selectCalendarMemoFilterTagUseCase: SelectCalendarMemoFilterTagUseCase,
    private val unselectCalendarMemoFilterTagUseCase: UnselectCalendarMemoFilterTagUseCase,
) : ViewModel() {
    private val tagPagingData: Flow<PagingData<io.github.taetae98coding.diary.core.model.tag.Tag>> = pageTagUseCase()
        .mapLatest { it.getOrDefault(PagingData.empty()) }
        .cachedIn(viewModelScope)

    private val selectedTagIds = getCalendarMemoFilterTagUseCase()
        .mapLatest { it.getOrDefault(emptyList()).toSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet(),
        )

    val filterTagPagingData: Flow<PagingData<CalendarFilterTagUiState>> = combine(tagPagingData, selectedTagIds) { pagingData, selectedIds ->
        pagingData.map { tag ->
            CalendarFilterTagUiState(
                id = tag.id,
                title = tag.detail.title,
                color = tag.detail.color,
                isSelected = tag.id in selectedIds,
            )
        }
    }.cachedIn(viewModelScope)

    fun select(tagId: Uuid) {
        viewModelScope.launch {
            selectCalendarMemoFilterTagUseCase(tagId)
        }
    }

    fun unselect(tagId: Uuid) {
        viewModelScope.launch {
            unselectCalendarMemoFilterTagUseCase(tagId)
        }
    }
}
