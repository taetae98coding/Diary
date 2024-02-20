package com.taetae98.diary.data.local.holiday.di

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.taetae98.diary.data.local.holiday.HolidayDatabase
import org.koin.core.component.inject

internal actual fun SqldelightModule.getSqlDriver(): SqlDriver {
    val context by inject<Context>()
    val schema = HolidayDatabase.Schema.synchronous()

    return AndroidSqliteDriver(
        schema = schema,
        context = context,
        name = HOLIDAY_DB_NAME,
        callback = object : AndroidSqliteDriver.Callback(schema) {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                db.setForeignKeyConstraintsEnabled(true)
            }
        }
    )
}