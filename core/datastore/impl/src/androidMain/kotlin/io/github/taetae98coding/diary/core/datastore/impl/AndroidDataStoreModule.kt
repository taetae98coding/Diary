package io.github.taetae98coding.diary.core.datastore.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.deviceProtectedDataStoreFile
import androidx.datastore.core.okio.OkioStorage
import io.github.taetae98coding.diary.core.datastore.api.entity.AccountMetaDataDataStoreEntity
import io.github.taetae98coding.diary.core.datastore.impl.serializer.AccountMetaDataSerializer
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
public class AndroidDataStoreModule {
    @Single
    internal fun providesAccountMetaDataDataStore(context: Context): DataStore<AccountMetaDataDataStoreEntity> {
        return DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = FileSystem.SYSTEM,
                serializer = AccountMetaDataSerializer,
                producePath = { context.deviceProtectedDataStoreFile("diary.json").toOkioPath() },
            ),
        )
    }
}
