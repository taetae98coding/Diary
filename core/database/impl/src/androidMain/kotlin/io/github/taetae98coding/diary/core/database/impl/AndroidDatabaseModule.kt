package io.github.taetae98coding.diary.core.database.impl

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.github.taetae98coding.diary.core.database.impl.di.DiaryDatabaseBuilder
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
@Configuration
public class AndroidDatabaseModule {
    @DiaryDatabaseBuilder
    @Factory
    internal fun providesDiaryDatabaseBuilder(context: Context): RoomDatabase.Builder<DiaryDatabase> {
        return Room.databaseBuilder<DiaryDatabase>(
            context = context,
            name = context.getDatabasePath("diary.db").absolutePath,
        ).setDriver(BundledSQLiteDriver())
    }
}
