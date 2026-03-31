package io.github.taetae98coding.diary.core.datastore.impl.datasource

import androidx.datastore.core.DataStore
import io.github.taetae98coding.diary.core.datastore.api.datasource.ListMemoFilterOptionDataStoreDataSource
import io.github.taetae98coding.diary.core.datastore.api.entity.FilterPresenceDataStoreEntity
import io.github.taetae98coding.diary.core.datastore.api.entity.ListMemoFilterOptionDataStoreEntity
import io.github.taetae98coding.diary.core.datastore.impl.ListMemoFilterOptionDataStore
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class ListMemoFilterOptionDataStoreDataSourceImpl(
    @param:ListMemoFilterOptionDataStore
    private val dataStore: DataStore<ListMemoFilterOptionDataStoreEntity>,
) : ListMemoFilterOptionDataStoreDataSource {
    override fun get(): Flow<ListMemoFilterOptionDataStoreEntity> {
        return dataStore.data
    }

    override suspend fun updateTagPresence(entity: FilterPresenceDataStoreEntity) {
        dataStore.updateData { it.copy(tagPresence = entity) }
    }

    override suspend fun updateDatePresence(entity: FilterPresenceDataStoreEntity) {
        dataStore.updateData { it.copy(datePresence = entity) }
    }
}
