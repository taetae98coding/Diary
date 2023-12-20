package com.taetae98.diary.local.impl.di

import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.local.impl.memo.MemoDao
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
internal actual class DatabaseModule {
    @Factory
    actual fun providesMemoDao(): MemoDao {
        return object : MemoDao {
            override suspend fun upsert(memo: MemoDto) {

            }
        }
    }
}