package io.github.taetae98coding.diary.presenter.memo.api

import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.github.taetae98coding.diary.core.model.memo.Memo
import kotlin.uuid.Uuid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

public class MemoListStateHolder(
    private val coroutineScope: CoroutineScope,
    private val strategy: MemoListStrategy,
) {
    private val _effect = MutableStateFlow<MemoListEffect>(MemoListEffect.None)
    public val effect: StateFlow<MemoListEffect> = _effect.asStateFlow()

    public val pagingData: Flow<PagingData<Memo>> = strategy.page()
        .mapLatest { it.getOrDefault(PagingData.empty()) }
        .cachedIn(coroutineScope)

    public fun finish(memoId: Uuid) {
        coroutineScope.launch {
            strategy.finish(memoId)
                .onSuccess { _effect.value = MemoListEffect.FinishComplete(memoId) }
        }
    }

    public fun restart(memoId: Uuid) {
        coroutineScope.launch {
            strategy.restart(memoId)
        }
    }

    public fun delete(memoId: Uuid) {
        coroutineScope.launch {
            strategy.delete(memoId)
                .onSuccess { _effect.value = MemoListEffect.DeleteComplete(memoId) }
        }
    }

    public fun restore(memoId: Uuid) {
        coroutineScope.launch {
            strategy.restore(memoId)
        }
    }

    public fun consumeEffect() {
        _effect.value = MemoListEffect.None
    }
}
