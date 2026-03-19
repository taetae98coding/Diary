package io.github.taetae98coding.diary.core.datastore.api.datasource

import io.github.taetae98coding.diary.core.datastore.api.entity.AccountMetaDataDataStoreEntity
import kotlinx.coroutines.flow.Flow

public interface AccountMetaDataDataStoreDataSource {
    public fun get(): Flow<AccountMetaDataDataStoreEntity?>

    public suspend fun upsert(entity: AccountMetaDataDataStoreEntity)
}
