package io.github.taetae98coding.diary.core.holiday.preferences.memory

import io.github.taetae98coding.diary.core.holiday.preferences.HolidayPreferences
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
@ComponentScan
public class HolidayPreferencesMemoryModule {
    @Singleton
    internal fun providesHolidayPreferences(): HolidayPreferences {
        return HolidayMemoryPreferences
    }
}
