package io.github.taetae98coding.diary.feature.memo

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.DialogSceneStrategy
import io.github.taetae98coding.diary.core.navigation.MemoAddNavKey
import io.github.taetae98coding.diary.core.navigation.MemoAddTagNavKey
import io.github.taetae98coding.diary.core.navigation.MemoDetailNavKey
import io.github.taetae98coding.diary.core.navigation.MemoDetailTagNavKey
import io.github.taetae98coding.diary.core.navigation.MemoHomeNavKey
import io.github.taetae98coding.diary.core.navigation.MemoListFilterNavKey
import io.github.taetae98coding.diary.core.navigation.TagAddNavKey
import io.github.taetae98coding.diary.core.navigation.TagDetailNavKey
import io.github.taetae98coding.diary.core.navigation.argument.MemoId
import io.github.taetae98coding.diary.core.navigation.argument.TagId
import io.github.taetae98coding.diary.feature.memo.add.MemoAddScreen
import io.github.taetae98coding.diary.feature.memo.add.MemoAddTagScreen
import io.github.taetae98coding.diary.feature.memo.detail.MemoDetailScreen
import io.github.taetae98coding.diary.feature.memo.detail.MemoDetailTagScreen
import io.github.taetae98coding.diary.feature.memo.di.MemoAddScope
import io.github.taetae98coding.diary.feature.memo.di.MemoDetailScope
import io.github.taetae98coding.diary.feature.memo.di.MemoDetailTagScope
import io.github.taetae98coding.diary.feature.memo.di.MemoHomeScope
import io.github.taetae98coding.diary.feature.memo.home.MemoHomeScreen
import io.github.taetae98coding.diary.feature.memo.home.MemoListFilterDialog
import io.github.taetae98coding.diary.library.koin.compose.rememberKoinScope
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

public fun EntryProviderScope<NavKey>.memoEntry(backStack: NavBackStack<NavKey>) {
    entry<MemoHomeNavKey> {
        val scope = rememberKoinScope<MemoHomeScope>(scopeId = MemoHomeScope.DEFAULT_ID, autoClose = true)

        MemoHomeScreen(
            navigateToMemoAdd = { backStack.add(MemoAddNavKey()) },
            navigateToMemoDetail = { backStack.add(MemoDetailNavKey(MemoId(it))) },
            navigateToFilter = { backStack.add(MemoListFilterNavKey) },
            memoListStateHolder = koinInject(scope = scope),
            filterStateHolder = koinInject(scope = scope),
        )
    }

    entry<MemoListFilterNavKey>(
        metadata = DialogSceneStrategy.dialog(),
    ) {
        MemoListFilterDialog(
            navigateUp = backStack::removeLastOrNull,
            navigateToTagAdd = { backStack.add(TagAddNavKey) },
        )
    }

    entry<MemoAddNavKey> { navKey ->
        val scope = rememberKoinScope<MemoAddScope>(scopeId = MemoAddScope.DEFAULT_ID, autoClose = true)

        MemoAddScreen(
            navKey = navKey,
            navigateUp = backStack::removeLastOrNull,
            navigateToMemoAddTag = { backStack.add(MemoAddTagNavKey) },
            navigateToTagDetail = { backStack.add(TagDetailNavKey(TagId(it))) },
            stateHolder = koinInject(scope = scope) { parametersOf(navKey.tagId) },
        )
    }

    entry<MemoAddTagNavKey> {
        val scope = rememberKoinScope<MemoAddScope>(scopeId = MemoAddScope.DEFAULT_ID, autoClose = false)

        MemoAddTagScreen(
            navigateUp = backStack::removeLastOrNull,
            navigateToTagAdd = { backStack.add(TagAddNavKey) },
            stateHolder = koinInject(scope = scope),
        )
    }

    entry<MemoDetailNavKey> { navKey ->
        val scope = rememberKoinScope<MemoDetailScope>(scopeId = MemoDetailScope.DEFAULT_ID, autoClose = true)

        MemoDetailScreen(
            navigateUp = backStack::removeLastOrNull,
            navigateToMemoDetailTag = { backStack.add(MemoDetailTagNavKey(navKey.memoId)) },
            navigateToTagDetail = { backStack.add(TagDetailNavKey(TagId(it))) },
            stateHolder = koinInject(scope = scope) { parametersOf(navKey.memoId) },
        )
    }

    entry<MemoDetailTagNavKey> { navKey ->
        val scope = rememberKoinScope<MemoDetailTagScope>(scopeId = MemoDetailTagScope.DEFAULT_ID, autoClose = true)

        MemoDetailTagScreen(
            navigateUp = backStack::removeLastOrNull,
            navigateToTagAdd = { backStack.add(TagAddNavKey) },
            stateHolder = koinInject(scope = scope) { parametersOf(navKey.memoId) },
        )
    }
}
