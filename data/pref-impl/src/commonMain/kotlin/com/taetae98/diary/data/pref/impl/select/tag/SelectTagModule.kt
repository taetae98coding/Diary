package com.taetae98.diary.data.pref.impl.select.tag

import com.taetae98.diary.data.pref.api.SelectTagByMemoPrefDataSource
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.core.component.KoinComponent

@Module
internal class SelectTagModule : KoinComponent {
    @Singleton
    fun providesSelectTagByMemoPrefDataSource(): SelectTagByMemoPrefDataSource {
        return getSelectTagByMemoPrefDataSource()
    }
}