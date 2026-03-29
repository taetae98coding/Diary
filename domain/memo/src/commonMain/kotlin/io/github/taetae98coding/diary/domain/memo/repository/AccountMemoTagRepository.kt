package io.github.taetae98coding.diary.domain.memo.repository

import kotlin.uuid.Uuid

public interface AccountMemoTagRepository {
    public suspend fun updatePrimaryTag(
        memoId: Uuid,
        primaryTag: Uuid?,
    )

    public suspend fun upsertMemoTag(
        memoId: Uuid,
        tagId: Uuid,
        isMemoTag: Boolean,
    )
}
