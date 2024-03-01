package com.taetae98.diary.data.pref.impl.tag

import com.taetae98.diary.data.pref.api.TagPrefDataSource
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.core.component.KoinComponent

@Module
internal class TagModule : KoinComponent {
    @Singleton
    fun providesTagPrefDataSource(): TagPrefDataSource {
        return getTagPrefDataSource()
    }
}