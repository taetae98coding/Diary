package com.taetae98.diary.data.local.holiday.di

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.taetae98.diary.data.local.holiday.HolidayDatabase
import java.io.File
import java.util.Properties

internal actual fun SqldelightModule.getSqlDriver(): SqlDriver {
    return JdbcSqliteDriver(
        url = "jdbc:sqlite:$HOLIDAY_DB_NAME",
        properties = Properties().apply { put("foreign_keys", "true") }
    ).also {
        if (!File(HOLIDAY_DB_NAME).exists()) {
            HolidayDatabase.Schema.synchronous().create(it)
        }
    }
}