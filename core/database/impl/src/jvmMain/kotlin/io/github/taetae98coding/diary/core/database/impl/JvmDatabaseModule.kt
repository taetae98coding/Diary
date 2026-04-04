package io.github.taetae98coding.diary.core.database.impl

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.github.taetae98coding.diary.core.database.impl.di.DiaryDatabaseBuilder
import java.io.File
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided

@Module
@Configuration
public class JvmDatabaseModule {
    @DiaryDatabaseBuilder
    @Factory
    internal fun providesDiaryDatabaseBuilder(
        @Provided
        @DatabaseParentFile
        parentFile: File,
    ): RoomDatabase.Builder<DiaryDatabase> {
        val file = File(parentFile, "diary.db")
            .apply { parentFile.mkdirs() }

        return Room.databaseBuilder<DiaryDatabase>(name = file.absolutePath)
            .setDriver(BundledSQLiteDriver())
    }
}
