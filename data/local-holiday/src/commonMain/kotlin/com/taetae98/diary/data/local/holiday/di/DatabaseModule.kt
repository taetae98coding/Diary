package com.taetae98.diary.data.local.holiday.di

import app.cash.sqldelight.db.SqlDriver
import com.taetae98.diary.data.local.holiday.HolidayDatabase
import com.taetae98.diary.data.local.holiday.HolidayEntity
import com.taetae98.diary.data.local.holiday.adapter.LocalDateAdapter
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton

@Module
internal class DatabaseModule {
    @Singleton
    fun providesDatabase(
        @Named(SqldelightModule.HOLIDAY_DATABASE_DRIVER)
        driver: SqlDriver
    ): HolidayDatabase {
        return HolidayDatabase(
            driver = driver,
            HolidayEntityAdapter = HolidayEntity.Adapter(
                startAtAdapter = LocalDateAdapter,
                endAtAdapter = LocalDateAdapter,
            )
        )
    }
}