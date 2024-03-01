package com.taetae98.diary.data.local.impl.di

import app.cash.sqldelight.db.SqlDriver
import com.taetae98.diary.data.local.impl.DiaryDatabase
import com.taetae98.diary.data.local.impl.MemoEntity
import com.taetae98.diary.data.local.impl.TagEntity
import com.taetae98.diary.data.local.impl.adapter.InstantAdapter
import com.taetae98.diary.data.local.impl.adapter.LocalDateAdapter
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton

@Module
internal class DatabaseModule {
    @Singleton
    fun providesDatabase(
        @Named(SqldelightModule.DIARY_DATABASE_DRIVER)
        driver: SqlDriver
    ): DiaryDatabase {
        return DiaryDatabase(
            driver = driver,
            MemoEntityAdapter = MemoEntity.Adapter(
                dateRangeStartAdapter = LocalDateAdapter,
                dateRangeEndAdapter = LocalDateAdapter,
                updateAtAdapter = InstantAdapter,
            ),
            TagEntityAdapter = TagEntity.Adapter(
                updateAtAdapter = InstantAdapter,
            ),
        )
    }
}