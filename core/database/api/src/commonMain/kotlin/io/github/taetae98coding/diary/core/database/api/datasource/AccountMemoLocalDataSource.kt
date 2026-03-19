package io.github.taetae98coding.diary.core.database.api.datasource

import io.github.taetae98coding.diary.core.database.api.entity.AccountMemoLocalEntity

public interface AccountMemoLocalDataSource {
    public suspend fun upsert(entity: AccountMemoLocalEntity)
    public suspend fun upsert(entities: Collection<AccountMemoLocalEntity>)
}
