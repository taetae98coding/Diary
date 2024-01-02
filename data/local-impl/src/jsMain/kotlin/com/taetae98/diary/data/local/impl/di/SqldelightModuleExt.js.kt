package com.taetae98.diary.data.local.impl.di

import app.cash.sqldelight.db.SqlDriver

internal actual fun SqldelightModule.getSqlDriver(): SqlDriver {
    return EmptySqlDriver()
}