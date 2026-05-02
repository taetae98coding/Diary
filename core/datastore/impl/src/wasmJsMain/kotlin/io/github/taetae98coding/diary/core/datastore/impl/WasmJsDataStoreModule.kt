package io.github.taetae98coding.diary.core.datastore.impl

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.WebOpfsStorage
import androidx.datastore.core.okio.WebSerializer
import io.github.taetae98coding.diary.core.datastore.api.entity.AccountMetaDataDataStoreEntity
import io.github.taetae98coding.diary.core.datastore.api.entity.ListMemoFilterOptionDataStoreEntity
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
public class WasmJsDataStoreModule {
    @Single
    @AccountMetaDataDataStore
    internal fun providesAccountMetaDataDataStore(): DataStore<AccountMetaDataDataStoreEntity> {
        return DataStoreFactory.create(
            storage = WebOpfsStorage(
                serializer = WebSerializer(
                    kSerializer = AccountMetaDataDataStoreEntity.serializer(),
                    defaultValue = AccountMetaDataDataStoreEntity(),
                ),
                name = "account_metadata",
            ),
        )
    }

    @Single
    @ListMemoFilterOptionDataStore
    internal fun providesListMemoFilterOptionDataStore(): DataStore<ListMemoFilterOptionDataStoreEntity> {
        return DataStoreFactory.create(
            storage = WebOpfsStorage(
                serializer = WebSerializer(
                    kSerializer = ListMemoFilterOptionDataStoreEntity.serializer(),
                    defaultValue = ListMemoFilterOptionDataStoreEntity(),
                ),
                name = "list_memo_filter_option",
            ),
        )
    }
}
