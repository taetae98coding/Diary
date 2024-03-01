package com.taetae98.diary.data.local.holiday.di

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.DatabaseConfiguration
import com.taetae98.diary.data.local.holiday.HolidayDatabase

internal actual fun SqldelightModule.getSqlDriver(): SqlDriver {
    return NativeSqliteDriver(
        schema = HolidayDatabase.Schema.synchronous(),
        name = HOLIDAY_DB_NAME,
        onConfiguration = { config ->
            config.copy(
                extendedConfig = DatabaseConfiguration.Extended(foreignKeyConstraints = true),
            )
        },
    )
}