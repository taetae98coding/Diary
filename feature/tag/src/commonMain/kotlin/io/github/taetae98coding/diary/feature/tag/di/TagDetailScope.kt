package io.github.taetae98coding.diary.feature.tag.di

import org.koin.core.annotation.Qualifier

@Qualifier
internal annotation class TagDetailScope {
    companion object {
        const val DEFAULT_ID = "TagDetailScope"
    }
}
