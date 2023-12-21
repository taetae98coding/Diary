package com.taetae98.diary.local.impl.di

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
internal expect class SqldelightModule() {
    @Factory
    fun provideSqlDriver(): SqlDriver
}