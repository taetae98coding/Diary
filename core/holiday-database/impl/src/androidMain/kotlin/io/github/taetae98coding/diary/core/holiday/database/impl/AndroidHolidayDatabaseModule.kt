package io.github.taetae98coding.diary.core.holiday.database.impl

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.github.taetae98coding.diary.core.holiday.database.impl.di.HolidayDatabaseBuilder
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
@Configuration
public class AndroidHolidayDatabaseModule {
    @HolidayDatabaseBuilder
    @Factory
    internal fun providesHolidayDatabaseBuilder(context: Context): RoomDatabase.Builder<HolidayDatabase> {
        return Room.databaseBuilder<HolidayDatabase>(
            context = context,
            name = context.getDatabasePath("holiday.db").absolutePath,
        ).setDriver(BundledSQLiteDriver())
    }
}
