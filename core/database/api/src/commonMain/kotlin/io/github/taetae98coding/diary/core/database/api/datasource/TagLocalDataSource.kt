package io.github.taetae98coding.diary.core.database.api.datasource

import io.github.taetae98coding.diary.core.database.api.entity.TagDetailLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface TagLocalDataSource {
    public suspend fun upsert(entity: TagLocalEntity)
    public suspend fun upsert(entities: Collection<TagLocalEntity>)

    public suspend fun updateDetail(
        tagId: Uuid,
        detail: TagDetailLocalEntity,
        updatedAt: Long,
    )

    public suspend fun updateFinish(
        tagId: Uuid,
        isFinished: Boolean,
        updatedAt: Long,
    )

    public suspend fun updateDelete(
        tagId: Uuid,
        isDeleted: Boolean,
        updatedAt: Long,
    )

    public fun get(tagId: Uuid): Flow<TagLocalEntity?>
}
