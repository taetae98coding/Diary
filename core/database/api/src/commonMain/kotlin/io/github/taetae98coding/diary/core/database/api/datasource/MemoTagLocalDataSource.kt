package io.github.taetae98coding.diary.core.database.api.datasource

import io.github.taetae98coding.diary.core.database.api.entity.MemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface MemoTagLocalDataSource {
    public suspend fun upsert(entities: Collection<MemoTagLocalEntity>)
    public fun get(memoId: Uuid): Flow<List<TagLocalEntity>>
}
