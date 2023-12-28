package com.taetae98.diary.local.impl.di

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
internal expect class SqldelightModule() {
    @Singleton
    fun provideSqlDriver(): SqlDriver
}