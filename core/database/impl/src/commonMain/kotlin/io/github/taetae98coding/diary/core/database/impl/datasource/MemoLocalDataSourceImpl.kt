package io.github.taetae98coding.diary.core.database.impl.datasource

import io.github.taetae98coding.diary.core.database.api.datasource.MemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.MemoDetailLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class MemoLocalDataSourceImpl(private val database: DiaryDatabase) : MemoLocalDataSource {
    override suspend fun upsert(entity: MemoLocalEntity) {
        database.memoDao().upsert(entity)
    }

    override suspend fun upsert(entities: Collection<MemoLocalEntity>) {
        database.memoDao().upsert(entities)
    }

    override suspend fun updateDetail(
        memoId: Uuid,
        detail: MemoDetailLocalEntity,
        updatedAt: Long,
    ) {
        database.memoDao().updateDetail(
            memoId = memoId,
            title = detail.title,
            description = detail.description,
            isAllDay = detail.isAllDay,
            start = detail.start,
            endInclusive = detail.endInclusive,
            color = detail.color,
            updatedAt = updatedAt,
        )
    }

    override suspend fun updateFinish(
        memoId: Uuid,
        isFinished: Boolean,
        updatedAt: Long,
    ) {
        database.memoDao().updateFinish(memoId, isFinished, updatedAt)
    }

    override suspend fun updateDelete(
        memoId: Uuid,
        isDeleted: Boolean,
        updatedAt: Long,
    ) {
        database.memoDao().updateDelete(memoId, isDeleted, updatedAt)
    }

    override suspend fun updatePrimaryTag(
        memoId: Uuid,
        primaryTag: Uuid?,
        updatedAt: Long,
    ) {
        database.memoDao().updatePrimaryTag(memoId, primaryTag, updatedAt)
    }

    override fun get(memoId: Uuid): Flow<MemoLocalEntity?> {
        return database.memoDao().get(memoId)
    }
}
