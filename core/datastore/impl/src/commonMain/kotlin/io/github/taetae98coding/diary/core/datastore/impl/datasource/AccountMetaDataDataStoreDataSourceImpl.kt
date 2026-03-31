package io.github.taetae98coding.diary.core.datastore.impl.datasource

import androidx.datastore.core.DataStore
import io.github.taetae98coding.diary.core.datastore.api.datasource.AccountMetaDataDataStoreDataSource
import io.github.taetae98coding.diary.core.datastore.api.entity.AccountMetaDataDataStoreEntity
import io.github.taetae98coding.diary.core.datastore.impl.AccountMetaDataDataStore
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class AccountMetaDataDataStoreDataSourceImpl(
    @param:AccountMetaDataDataStore
    private val dataStore: DataStore<AccountMetaDataDataStoreEntity>,
) : AccountMetaDataDataStoreDataSource {
    override fun get(): Flow<AccountMetaDataDataStoreEntity?> {
        return dataStore.data
    }

    override suspend fun upsert(entity: AccountMetaDataDataStoreEntity) {
        dataStore.updateData { entity }
    }
}
