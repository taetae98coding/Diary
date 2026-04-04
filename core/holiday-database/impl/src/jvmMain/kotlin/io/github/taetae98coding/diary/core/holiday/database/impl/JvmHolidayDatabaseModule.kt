package io.github.taetae98coding.diary.core.holiday.database.impl

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.github.taetae98coding.diary.core.holiday.database.impl.di.HolidayDatabaseBuilder
import java.io.File
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided

@Module
@Configuration
public class JvmHolidayDatabaseModule {
    @HolidayDatabaseBuilder
    @Factory
    internal fun providesHolidayDatabaseBuilder(
        @Provided
        @HolidayDatabaseParentFile
        parentFile: File,
    ): RoomDatabase.Builder<HolidayDatabase> {
        val file = File(parentFile, "holiday.db")
            .apply { parentFile.mkdirs() }

        return Room.databaseBuilder<HolidayDatabase>(name = file.absolutePath)
            .setDriver(BundledSQLiteDriver())
    }
}
