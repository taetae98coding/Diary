package com.taetae98.diary.local.impl.di

import app.cash.sqldelight.db.SqlDriver

internal const val DIARY_DB_NAME = "diary.db"

internal expect fun SqldelightModule.getSqlDriver(): SqlDriver