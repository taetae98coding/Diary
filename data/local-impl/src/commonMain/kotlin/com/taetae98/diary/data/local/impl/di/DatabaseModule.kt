package com.taetae98.diary.data.local.impl.di

import app.cash.sqldelight.db.SqlDriver
import com.taetae98.diary.core.coroutines.CoroutinesModule
import com.taetae98.diary.data.local.impl.DiaryDatabase
import com.taetae98.diary.data.local.impl.MemoEntity
import com.taetae98.diary.data.local.impl.adapter.InstantAdapter
import com.taetae98.diary.data.local.impl.adapter.MemoStateAdapter
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton

@Module
internal class DatabaseModule {
    @Singleton
    fun providesMemoDao(
        driver: SqlDriver
    ): DiaryDatabase {
        return DiaryDatabase(
            driver = driver,
            MemoEntityAdapter = MemoEntity.Adapter(
                updateAtAdapter = InstantAdapter,
                stateAdapter = MemoStateAdapter,
            )
        )
    }

    @Named(DATABASE_DISPATCHER)
    @Singleton
    fun providesDatabaseDispatcher(
        @Named(CoroutinesModule.IO)
        dispatcher: CoroutineDispatcher
    ): CoroutineDispatcher {
        return dispatcher
    }

    companion object {
        internal const val DATABASE_DISPATCHER = "databaseDispatcher"
    }
}