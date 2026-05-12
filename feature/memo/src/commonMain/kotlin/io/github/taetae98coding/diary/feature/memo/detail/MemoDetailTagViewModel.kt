@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.feature.memo.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.navigation.argument.MemoId
import io.github.taetae98coding.diary.domain.memo.usecase.AddMemoTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.GetMemoTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.GetMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.RemoveMemoTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.SelectPrimaryTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.UnselectPrimaryTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagUseCase
import io.github.taetae98coding.diary.feature.memo.common.MemoTagStateHolder
import io.github.taetae98coding.diary.feature.memo.common.MemoTagUiState
import kotlin.uuid.Uuid
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import org.koin.core.annotation.Provided

@KoinViewModel
internal class MemoDetailTagViewModel(
    @Provided
    private val memoId: MemoId,
    getMemoUseCase: GetMemoUseCase,
    getMemoTagUseCase: GetMemoTagUseCase,
    pageTagUseCase: PageTagUseCase,
    private val selectPrimaryTagUseCase: SelectPrimaryTagUseCase,
    private val unselectPrimaryTagUseCase: UnselectPrimaryTagUseCase,
    private val addMemoTagUseCase: AddMemoTagUseCase,
    private val removeMemoTagUseCase: RemoveMemoTagUseCase,
) : ViewModel(),
    MemoTagStateHolder {
    private val memo: StateFlow<Memo?> = getMemoUseCase(memoId.value)
        .mapLatest { it.getOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    private val memoTagIds: StateFlow<Set<Uuid>> = getMemoTagUseCase(memoId.value)
        .mapLatest { result -> result.getOrDefault(emptyList()) }
        .mapLatest { it.map(Tag::id).toSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet(),
        )

    private val tagPagingData: Flow<PagingData<Tag>> = pageTagUseCase()
        .mapLatest { it.getOrDefault(PagingData.empty()) }
        .cachedIn(viewModelScope)

    override val primaryPagingData: Flow<PagingData<MemoTagUiState>> = combine(tagPagingData, memo) { pagingData, memo ->
        pagingData.map { tag ->
            MemoTagUiState(
                tagId = tag.id,
                title = tag.detail.title,
                color = tag.detail.color,
                isPrimary = tag.id == memo?.primaryTag,
                isSelected = tag.id == memo?.primaryTag,
            )
        }
    }

    override val memoTagPagingData: Flow<PagingData<MemoTagUiState>> = combine(tagPagingData, memoTagIds, memo) { pagingData, memoTagIds, memo ->
        pagingData.map { tag ->
            MemoTagUiState(
                tagId = tag.id,
                title = tag.detail.title,
                color = tag.detail.color,
                isSelected = tag.id in memoTagIds,
                isPrimary = tag.id == memo?.primaryTag,
            )
        }
    }

    override fun selectPrimaryTag(tagId: Uuid) {
        viewModelScope.launch {
            selectPrimaryTagUseCase(memoId.value, tagId)
        }
    }

    override fun unselectPrimaryTag(tagId: Uuid) {
        viewModelScope.launch {
            unselectPrimaryTagUseCase(memoId.value)
        }
    }

    override fun selectMemoTag(tagId: Uuid) {
        viewModelScope.launch {
            addMemoTagUseCase(memoId.value, tagId)
        }
    }

    override fun unselectMemoTag(tagId: Uuid) {
        viewModelScope.launch {
            removeMemoTagUseCase(memoId.value, tagId)
        }
    }
}
