package io.github.taetae98coding.diary.feature.tag.di

import org.koin.core.annotation.Qualifier

@Qualifier
internal annotation class TagHomeScope {
    companion object {
        const val DEFAULT_ID = "TagHomeScope"
    }
}
