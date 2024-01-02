package com.taetae98.diary.data.local.impl.di

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.DatabaseConfiguration
import com.taetae98.diary.data.local.impl.DiaryDatabase

internal actual fun SqldelightModule.getSqlDriver(): SqlDriver {
    return NativeSqliteDriver(
        schema = DiaryDatabase.Schema.synchronous(),
        name = DIARY_DB_NAME,
        onConfiguration = { config ->
            config.copy(
                extendedConfig = DatabaseConfiguration.Extended(foreignKeyConstraints = true),
            )
        },
    )
}