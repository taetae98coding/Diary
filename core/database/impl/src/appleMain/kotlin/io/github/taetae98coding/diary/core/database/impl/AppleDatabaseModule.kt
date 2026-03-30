package io.github.taetae98coding.diary.core.database.impl

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.github.taetae98coding.diary.core.database.impl.di.DiaryDatabaseBuilder
import io.github.taetae98coding.diary.library.file.documentDirectory
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import platform.Foundation.NSFileManager

@Module
@Configuration
@OptIn(ExperimentalForeignApi::class)
public class AppleDatabaseModule {
    @DiaryDatabaseBuilder
    @Factory
    internal fun providesDiaryDatabaseBuilder(): RoomDatabase.Builder<DiaryDatabase> {
        return Room.databaseBuilder<DiaryDatabase>("${requireNotNull(NSFileManager.documentDirectory())}/diary.db")
            .setDriver(BundledSQLiteDriver())
    }
}
