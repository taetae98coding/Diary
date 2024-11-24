package io.github.taetae98coding.diary.core.holiday.preferences.datastore

import io.github.taetae98coding.diary.core.holiday.preferences.HolidayPreferences
import io.github.taetae98coding.diary.library.koin.datastore.getDataStore
import kotlinx.datetime.Clock
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.core.component.KoinComponent

@Module
@ComponentScan
public class HolidayDataStorePreferencesModule : KoinComponent {
	@Singleton
	internal fun providesHolidayPreferences(
		clock: Clock,
	): HolidayPreferences =
		HolidayDataStorePreferences(
			clock = clock,
			dataStore = getDataStore("holiday.preferences_pb"),
		)
}
