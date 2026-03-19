package io.github.taetae98coding.diary.feature.memo

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.taetae98coding.diary.core.navigation.MemoAddNavKey
import io.github.taetae98coding.diary.core.navigation.MemoDetailNavKey
import io.github.taetae98coding.diary.feature.memo.add.MemoAddScreen
import io.github.taetae98coding.diary.feature.memo.detail.MemoDetailScreen
import io.github.taetae98coding.diary.feature.memo.di.MemoAddScope
import io.github.taetae98coding.diary.feature.memo.di.MemoDetailScope
import io.github.taetae98coding.diary.library.koin.compose.rememberKoinScope
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

public fun EntryProviderScope<NavKey>.memoEntry(backStack: NavBackStack<NavKey>) {
    entry<MemoAddNavKey> { navKey ->
        val scope = rememberKoinScope<MemoAddScope>(scopeId = MemoAddScope.DEFAULT_ID, autoClose = true)

        MemoAddScreen(
            navKey = navKey,
            stateHolder = koinInject(scope = scope),
            navigateUp = backStack::removeLastOrNull,
        )
    }

    entry<MemoDetailNavKey> { navKey ->
        val scope = rememberKoinScope<MemoDetailScope>(scopeId = MemoDetailScope.DEFAULT_ID, autoClose = true)

        MemoDetailScreen(
            stateHolder = koinInject(scope = scope) {
                parametersOf(navKey.memoId)
            },
            navigateUp = backStack::removeLastOrNull,
        )
    }
}
