package com.taetae98.diary.local.impl.di

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.core.component.KoinComponent

@Module
internal class SqldelightModule : KoinComponent {
    @Singleton
    fun provideSqlDriver(): SqlDriver {
        return getSqlDriver()
    }
}