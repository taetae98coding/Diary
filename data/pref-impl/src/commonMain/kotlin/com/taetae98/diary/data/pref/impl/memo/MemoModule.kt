package com.taetae98.diary.data.pref.impl.memo

import com.taetae98.diary.data.pref.api.MemoPrefDataSource
import com.taetae98.diary.data.pref.api.MemoTagPrefDataSource
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.core.component.KoinComponent

@Module
internal class MemoModule : KoinComponent {
    @Singleton
    fun providesMemoPrefDataSource(): MemoPrefDataSource {
        return getMemoPrefDataSource()
    }

    @Singleton
    fun providesMemoTagPrefDataSource(): MemoTagPrefDataSource {
        return getMemoTagPrefDataSource()
    }
}