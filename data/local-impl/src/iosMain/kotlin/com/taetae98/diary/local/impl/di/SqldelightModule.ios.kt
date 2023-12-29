package com.taetae98.diary.local.impl.di

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.DatabaseConfiguration
import com.taetae98.diary.data.local.impl.DiaryDatabase
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
internal actual class SqldelightModule {
    @Singleton
    actual fun provideSqlDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = DiaryDatabase.Schema.synchronous(),
            name = "diary.db",
            onConfiguration = { config ->
                config.copy(
                    extendedConfig = DatabaseConfiguration.Extended(foreignKeyConstraints = true),
                )
            },
        )
    }
}