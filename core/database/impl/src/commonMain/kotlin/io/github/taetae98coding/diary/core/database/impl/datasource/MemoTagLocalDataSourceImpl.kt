package io.github.taetae98coding.diary.core.database.impl.datasource

import io.github.taetae98coding.diary.core.database.api.datasource.MemoTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.MemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class MemoTagLocalDataSourceImpl(private val database: DiaryDatabase) : MemoTagLocalDataSource {
    override suspend fun upsert(entities: Collection<MemoTagLocalEntity>) {
        database.memoTagDao().upsert(entities)
    }

    override fun get(memoId: Uuid): Flow<List<TagLocalEntity>> {
        return database.memoTagDao().get(memoId)
    }
}
