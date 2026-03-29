package io.github.taetae98coding.diary.core.database.impl.datasource

import io.github.taetae98coding.diary.core.database.api.datasource.TagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.TagDetailLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class TagLocalDataSourceImpl(private val database: DiaryDatabase) : TagLocalDataSource {
    override suspend fun upsert(entity: TagLocalEntity) {
        database.tagDao().upsert(entity)
    }

    override suspend fun upsert(entities: Collection<TagLocalEntity>) {
        database.tagDao().upsert(entities)
    }

    override suspend fun updateDetail(
        tagId: Uuid,
        detail: TagDetailLocalEntity,
        updatedAt: Long,
    ) {
        database.tagDao().updateDetail(
            tagId = tagId,
            title = detail.title,
            description = detail.description,
            color = detail.color,
            updatedAt = updatedAt,
        )
    }

    override suspend fun updateFinish(
        tagId: Uuid,
        isFinished: Boolean,
        updatedAt: Long,
    ) {
        database.tagDao().updateFinish(tagId, isFinished, updatedAt)
    }

    override suspend fun updateDelete(
        tagId: Uuid,
        isDeleted: Boolean,
        updatedAt: Long,
    ) {
        database.tagDao().updateDelete(tagId, isDeleted, updatedAt)
    }

    override fun get(tagId: Uuid): Flow<TagLocalEntity?> {
        return database.tagDao().get(tagId)
    }
}
