package io.github.taetae98coding.diary.core.database.impl

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import io.github.taetae98coding.diary.core.datastore.api.entity.AccountMetaDataDataStoreEntity
import io.github.taetae98coding.diary.core.datastore.impl.serializer.AccountMetaDataSerializer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import okio.FileSystem
import okio.Path.Companion.toPath
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@Module
@Configuration
@OptIn(ExperimentalForeignApi::class)
public class AppleDataStoreModule {
    @Single
    internal fun providesAccountMetaDataDataStore(): DataStore<AccountMetaDataDataStoreEntity> {
        return DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = FileSystem.SYSTEM,
                serializer = AccountMetaDataSerializer,
                producePath = { "${requireNotNull(NSFileManager.documentDirectory())}/diary.json".toPath() },
            ),
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun NSFileManager.Companion.documentDirectory(
    appropriateForURL: NSURL? = null,
    create: Boolean = false,
    error: CPointer<ObjCObjectVar<NSError?>>? = null,
): String? {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = appropriateForURL,
        create = create,
        error = error,
    )

    return documentDirectory?.path
}
