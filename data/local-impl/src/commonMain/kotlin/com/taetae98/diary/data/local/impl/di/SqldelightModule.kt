package com.taetae98.diary.data.local.impl.di

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton
import org.koin.core.component.KoinComponent

@Module
internal class SqldelightModule : KoinComponent {
    @Named(DIARY_DATABASE_DRIVER)
    @Singleton
    fun provideSqlDriver(): SqlDriver {
        return getSqlDriver()
    }

    companion object {
        const val DIARY_DATABASE_DRIVER = "diaryDatabaseDriver"
    }
}