package io.github.taetae98coding.diary.presenter.memo.api

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.navigation.argument.MemoId
import kotlin.uuid.Uuid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

public class MemoDetailTagStateHolder(
    private val coroutineScope: CoroutineScope,
    private val memoId: MemoId,
    private val strategy: MemoDetailTagStrategy,
) : MemoTagStateHolder {
    private val memo: StateFlow<Memo?> = strategy.get(memoId.value)
        .mapLatest { it.getOrNull() }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    private val memoTagIds: StateFlow<Set<Uuid>> = strategy.getMemoTag(memoId.value)
        .mapLatest { result -> result.getOrDefault(emptyList()) }
        .mapLatest { it.map(Tag::id).toSet() }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet(),
        )

    private val tagPagingData: Flow<PagingData<Tag>> = strategy.pageTag()
        .mapLatest { it.getOrDefault(PagingData.empty()) }
        .cachedIn(coroutineScope)

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
        coroutineScope.launch {
            strategy.selectPrimaryTag(memoId.value, tagId)
        }
    }

    override fun unselectPrimaryTag(tagId: Uuid) {
        coroutineScope.launch {
            strategy.unselectPrimaryTag(memoId.value)
        }
    }

    override fun selectMemoTag(tagId: Uuid) {
        coroutineScope.launch {
            strategy.addMemoTag(memoId.value, tagId)
        }
    }

    override fun unselectMemoTag(tagId: Uuid) {
        coroutineScope.launch {
            strategy.removeMemoTag(memoId.value, tagId)
        }
    }
}
