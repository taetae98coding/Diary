package io.github.taetae98coding.diary.domain.memo.repository

import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import kotlin.uuid.Uuid

public interface AccountMemoRepository {
    public suspend fun add(
        accountId: Uuid,
        detail: MemoDetail,
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
}
