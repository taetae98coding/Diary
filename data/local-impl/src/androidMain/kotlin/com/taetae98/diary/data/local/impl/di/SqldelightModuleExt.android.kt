package com.taetae98.diary.data.local.impl.di

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.taetae98.diary.data.local.impl.DiaryDatabase
import org.koin.core.component.inject

internal actual fun SqldelightModule.getSqlDriver(): SqlDriver {
    val context by inject<Context>()
    val scheme = DiaryDatabase.Schema.synchronous()

    return AndroidSqliteDriver(
        schema = scheme,
        context = context,
        name = DIARY_DB_NAME,
        callback = object : AndroidSqliteDriver.Callback(scheme) {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                db.setForeignKeyConstraintsEnabled(true)
            }
        }
    )
}