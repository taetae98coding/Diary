package com.taetae98.diary.data.pref.impl.memo

import com.taetae98.diary.pref.api.MemoPrefDataSource
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
internal expect class MemoModule() {
    @Singleton
    fun providesMemoPrefDataSource(): MemoPrefDataSource
}