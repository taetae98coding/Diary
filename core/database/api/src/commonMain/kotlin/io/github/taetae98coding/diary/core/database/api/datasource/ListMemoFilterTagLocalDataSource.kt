package io.github.taetae98coding.diary.core.database.api.datasource

import io.github.taetae98coding.diary.core.database.api.entity.ListMemoFilterTagLocalEntity

public interface ListMemoFilterTagLocalDataSource {
    public suspend fun upsert(entity: ListMemoFilterTagLocalEntity)
    public suspend fun delete(entity: ListMemoFilterTagLocalEntity)
}
