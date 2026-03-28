package io.github.taetae98coding.diary.core.database.api.datasource

import io.github.taetae98coding.diary.core.database.api.entity.MemoDetailLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface MemoLocalDataSource {
    public suspend fun upsert(entity: MemoLocalEntity)
    public suspend fun upsert(entities: Collection<MemoLocalEntity>)

    public suspend fun updateDetail(
        memoId: Uuid,
        detail: MemoDetailLocalEntity,
        updatedAt: Long,
    )

    public suspend fun updateFinish(
        memoId: Uuid,
        isFinished: Boolean,
        updatedAt: Long,
    )

    public suspend fun updateDelete(
        memoId: Uuid,
        isDeleted: Boolean,
        updatedAt: Long,
    )

    public suspend fun updatePrimaryTag(
        memoId: Uuid,
        primaryTag: Uuid?,
        updatedAt: Long,
    )

    public fun get(memoId: Uuid): Flow<MemoLocalEntity?>
}
