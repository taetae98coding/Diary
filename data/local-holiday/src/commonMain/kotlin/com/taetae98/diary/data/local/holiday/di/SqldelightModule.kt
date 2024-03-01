package com.taetae98.diary.data.local.holiday.di

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton
import org.koin.core.component.KoinComponent

@Module
internal class SqldelightModule : KoinComponent {
    @Named(HOLIDAY_DATABASE_DRIVER)
    @Singleton
    fun provideSqlDriver(): SqlDriver {
        return getSqlDriver()
    }

    companion object {
        const val HOLIDAY_DATABASE_DRIVER = "holidayDatabaseDriver"
    }
}