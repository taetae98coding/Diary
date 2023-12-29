package com.taetae98.diary.local.impl.di

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.taetae98.diary.data.local.impl.DiaryDatabase
import java.io.File
import java.util.Properties

internal actual fun SqldelightModule.getSqlDriver(): SqlDriver {
    return JdbcSqliteDriver(
        url = "jdbc:sqlite:$DIARY_DB_NAME",
        properties = Properties().apply { put("foreign_keys", "true") }
    ).also {
        if (!File(DIARY_DB_NAME).exists()) {
            DiaryDatabase.Schema.synchronous().create(it)
        }
    }
}