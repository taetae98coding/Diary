package io.github.taetae98coding.diary.core.datastore.impl

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import io.github.taetae98coding.diary.core.datastore.api.entity.AccountMetaDataDataStoreEntity
import io.github.taetae98coding.diary.core.datastore.impl.serializer.AccountMetaDataSerializer
import java.io.File
import okio.FileSystem
import okio.Path.Companion.toPath
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
public class JvmDataStoreModule {
    @Single
    internal fun providesAccountMetaDataDataStore(
        @DataStoreParentFile
        parentFile: File,
    ): DataStore<AccountMetaDataDataStoreEntity> {
        return DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = FileSystem.SYSTEM,
                serializer = AccountMetaDataSerializer,
                producePath = { File(parentFile, "account_metadata.json").absolutePath.toPath() },
            ),
        )
    }
}
