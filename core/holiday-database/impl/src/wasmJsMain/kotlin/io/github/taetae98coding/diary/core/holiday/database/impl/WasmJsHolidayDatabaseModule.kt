package io.github.taetae98coding.diary.core.holiday.database.impl

import androidx.room3.Room
import androidx.room3.RoomDatabase
import io.github.taetae98coding.diary.core.holiday.database.impl.di.HolidayDatabaseBuilder
import io.github.taetae98coding.diary.library.sqlite.wasm.worker.createSqliteWasmWorkerDriver
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
@Configuration
public class WasmJsHolidayDatabaseModule {
    @HolidayDatabaseBuilder
    @Factory
    internal fun providesHolidayDatabaseBuilder(): RoomDatabase.Builder<HolidayDatabase> {
        return Room.databaseBuilder<HolidayDatabase>(name = "holiday.db")
            .setDriver(createSqliteWasmWorkerDriver())
    }
}
