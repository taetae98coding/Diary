package io.github.taetae98coding.diary.core.holiday.database.memory

import io.github.taetae98coding.diary.core.holiday.database.HolidayDao
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
@ComponentScan
public class HolidayMemoryDatabaseModule {
    @Singleton
    internal fun providesHolidayDao(): HolidayDao {
        return HolidayMemoryDao
    }
}
