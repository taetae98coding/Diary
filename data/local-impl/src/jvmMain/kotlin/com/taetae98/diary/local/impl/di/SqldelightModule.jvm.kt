package com.taetae98.diary.local.impl.di

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.taetae98.diary.data.local.impl.DiaryDatabase
import java.io.File
import java.util.Properties
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
internal actual class SqldelightModule {
    @Singleton
    actual fun provideSqlDriver(): SqlDriver {
        return JdbcSqliteDriver(
            url = "jdbc:sqlite:diary.db",
            properties = Properties().apply { put("foreign_keys", "true") }
        ).also {
            if (!File("diary.db").exists()) {
                DiaryDatabase.Schema.synchronous().create(it)
            }
        }
    }
}