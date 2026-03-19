package io.github.taetae98coding.diary.core.database.impl

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@Module
@Configuration
@OptIn(ExperimentalForeignApi::class)
public class AppleDatabaseModule {
    @Factory
    internal fun providesDiaryDatabaseBuilder(): RoomDatabase.Builder<DiaryDatabase> {
        return Room.databaseBuilder<DiaryDatabase>("${requireNotNull(NSFileManager.documentDirectory())}/diary.db")
            .setDriver(BundledSQLiteDriver())
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
