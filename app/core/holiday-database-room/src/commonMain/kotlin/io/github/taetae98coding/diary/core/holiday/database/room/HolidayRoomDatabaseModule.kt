package io.github.taetae98coding.diary.core.holiday.database.room

import io.github.taetae98coding.diary.library.koin.room.getDatabaseBuilder
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.core.component.KoinComponent

@Module
@ComponentScan
public class HolidayRoomDatabaseModule : KoinComponent {
	@Singleton
	internal fun providesHolidayDatabase(): HolidayDatabase =
		getDatabaseBuilder<HolidayDatabase>("holiday.db")
			.build()
}
