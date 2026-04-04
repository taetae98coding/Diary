package io.github.taetae98coding.diary.core.datastore.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.deviceProtectedDataStoreFile
import androidx.datastore.core.okio.OkioStorage
import io.github.taetae98coding.diary.core.datastore.api.entity.AccountMetaDataDataStoreEntity
import io.github.taetae98coding.diary.core.datastore.api.entity.ListMemoFilterOptionDataStoreEntity
import io.github.taetae98coding.diary.core.datastore.impl.serializer.AccountMetaDataSerializer
import io.github.taetae98coding.diary.core.datastore.impl.serializer.ListMemoFilterOptionSerializer
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
public class AndroidDataStoreModule {
    @Single
    @AccountMetaDataDataStore
    internal fun providesAccountMetaDataDataStore(context: Context): DataStore<AccountMetaDataDataStoreEntity> {
        return DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = FileSystem.SYSTEM,
                serializer = AccountMetaDataSerializer,
                producePath = { context.deviceProtectedDataStoreFile("diary.json").toOkioPath() },
            ),
        )
    }

    @Single
    @ListMemoFilterOptionDataStore
    internal fun providesListMemoFilterOptionDataStore(context: Context): DataStore<ListMemoFilterOptionDataStoreEntity> {
        return DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = FileSystem.SYSTEM,
                serializer = ListMemoFilterOptionSerializer,
                producePath = { context.deviceProtectedDataStoreFile("list_memo_filter_option.json").toOkioPath() },
            ),
        )
    }
}
