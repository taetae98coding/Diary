package io.github.taetae98coding.diary.presenter.memo.api

import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.model.memo.Memo
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface MemoListStrategy {
    public fun page(): Flow<Result<PagingData<Memo>>>
    public suspend fun finish(memoId: Uuid): Result<Unit>
    public suspend fun restart(memoId: Uuid): Result<Unit>
    public suspend fun delete(memoId: Uuid): Result<Unit>
    public suspend fun restore(memoId: Uuid): Result<Unit>
}
