package io.github.taetae98coding.diary.domain.memo.repository

import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface AccountMemoRepository {
    public suspend fun add(
        accountId: Uuid,
        detail: MemoDetail,
        primaryTag: Uuid?,
        memoTagIds: Set<Uuid>,
    )

    public suspend fun updateDetail(
        memoId: Uuid,
        detail: MemoDetail,
    )

    public suspend fun updateFinish(
        memoId: Uuid,
        isFinished: Boolean,
    )

    public suspend fun updateDelete(
        memoId: Uuid,
        isDeleted: Boolean,
    )

    public fun pageByTag(
        accountId: Uuid,
        tagId: Uuid,
    ): Flow<PagingData<Memo>>
}
