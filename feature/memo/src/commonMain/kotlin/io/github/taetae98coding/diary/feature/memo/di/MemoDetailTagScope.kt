package io.github.taetae98coding.diary.feature.memo.di

import org.koin.core.annotation.Qualifier

@Qualifier
internal annotation class MemoDetailTagScope {
    companion object {
        const val DEFAULT_ID = "MemoDetailTagScope"
    }
}
