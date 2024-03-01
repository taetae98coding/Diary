package com.taetae98.diary.data.local.holiday.di

import app.cash.sqldelight.db.SqlDriver

internal const val HOLIDAY_DB_NAME = "holiday.db"

internal expect fun SqldelightModule.getSqlDriver(): SqlDriver