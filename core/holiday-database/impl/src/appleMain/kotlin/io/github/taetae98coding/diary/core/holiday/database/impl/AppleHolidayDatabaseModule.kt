package io.github.taetae98coding.diary.core.holiday.database.impl

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.github.taetae98coding.diary.core.holiday.database.impl.di.HolidayDatabaseBuilder
import io.github.taetae98coding.diary.library.file.documentDirectory
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import platform.Foundation.NSFileManager

@Module
@Configuration
@OptIn(ExperimentalForeignApi::class)
public class AppleHolidayDatabaseModule {
    @HolidayDatabaseBuilder
    @Factory
    internal fun providesHolidayDatabaseBuilder(): RoomDatabase.Builder<HolidayDatabase> {
        return Room.databaseBuilder<HolidayDatabase>("${requireNotNull(NSFileManager.documentDirectory())}/holiday.db")
            .setDriver(BundledSQLiteDriver())
    }
}
