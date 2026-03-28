package io.github.taetae98coding.diary.presenter.memo.api

import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.tag.Tag
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface MemoDetailStrategy {
    public fun get(memoId: Uuid): Flow<Result<Memo?>>

    public fun getMemoTag(memoId: Uuid): Flow<Result<List<Tag>>>
    public suspend fun update(
        memoId: Uuid,
        detail: MemoDetail,
    ): Result<Unit>

    public suspend fun finish(memoId: Uuid): Result<Unit>

    public suspend fun restart(memoId: Uuid): Result<Unit>

    public suspend fun delete(memoId: Uuid): Result<Unit>
}
