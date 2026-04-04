package io.github.taetae98coding.diary.core.datastore.api.datasource

import io.github.taetae98coding.diary.core.datastore.api.entity.FilterPresenceDataStoreEntity
import io.github.taetae98coding.diary.core.datastore.api.entity.ListMemoFilterOptionDataStoreEntity
import kotlinx.coroutines.flow.Flow

public interface ListMemoFilterOptionDataStoreDataSource {
    public fun get(): Flow<ListMemoFilterOptionDataStoreEntity>

    public suspend fun updateTagPresence(entity: FilterPresenceDataStoreEntity)
    public suspend fun updateDatePresence(entity: FilterPresenceDataStoreEntity)
}
