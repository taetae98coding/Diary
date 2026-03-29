package io.github.taetae98coding.diary.presenter.memo.api

import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.tag.Tag
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface MemoDetailTagStrategy {
    public fun get(memoId: Uuid): Flow<Result<Memo?>>

    public fun pageTag(): Flow<Result<PagingData<Tag>>>

    public fun getMemoTag(memoId: Uuid): Flow<Result<List<Tag>>>

    public suspend fun selectPrimaryTag(
        memoId: Uuid,
        tagId: Uuid,
    ): Result<Unit>

    public suspend fun unselectPrimaryTag(memoId: Uuid): Result<Unit>

    public suspend fun addMemoTag(
        memoId: Uuid,
        tagId: Uuid,
    ): Result<Unit>

    public suspend fun removeMemoTag(
        memoId: Uuid,
        tagId: Uuid,
    ): Result<Unit>
}
