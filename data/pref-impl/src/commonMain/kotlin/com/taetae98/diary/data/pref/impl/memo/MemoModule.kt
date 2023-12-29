package com.taetae98.diary.data.pref.impl.memo

import com.taetae98.diary.pref.api.MemoPrefDataSource
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.core.component.KoinComponent

@Module
internal class MemoModule : KoinComponent {
    @Singleton
    fun providesMemoPrefDataSource(): MemoPrefDataSource {
        return getMemoPrefDataSource()
    }
}