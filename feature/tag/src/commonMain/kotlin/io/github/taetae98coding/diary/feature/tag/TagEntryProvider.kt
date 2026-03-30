package io.github.taetae98coding.diary.feature.tag

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.taetae98coding.diary.core.navigation.MemoAddNavKey
import io.github.taetae98coding.diary.core.navigation.MemoDetailNavKey
import io.github.taetae98coding.diary.core.navigation.TagAddNavKey
import io.github.taetae98coding.diary.core.navigation.TagDetailNavKey
import io.github.taetae98coding.diary.core.navigation.TagHomeNavKey
import io.github.taetae98coding.diary.core.navigation.TagMemoNavKey
import io.github.taetae98coding.diary.core.navigation.argument.MemoId
import io.github.taetae98coding.diary.core.navigation.argument.TagId
import io.github.taetae98coding.diary.feature.tag.add.TagAddScreen
import io.github.taetae98coding.diary.feature.tag.detail.TagDetailScreen
import io.github.taetae98coding.diary.feature.tag.di.TagAddScope
import io.github.taetae98coding.diary.feature.tag.di.TagDetailScope
import io.github.taetae98coding.diary.feature.tag.di.TagHomeScope
import io.github.taetae98coding.diary.feature.tag.di.TagMemoScope
import io.github.taetae98coding.diary.feature.tag.home.TagHomeScreen
import io.github.taetae98coding.diary.feature.tag.memo.TagMemoScreen
import io.github.taetae98coding.diary.library.koin.compose.rememberKoinScope
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

public fun EntryProviderScope<NavKey>.tagEntry(backStack: NavBackStack<NavKey>) {
    entry<TagHomeNavKey> {
        val scope = rememberKoinScope<TagHomeScope>(scopeId = TagHomeScope.DEFAULT_ID, autoClose = true)

        TagHomeScreen(
            navigateToTagAdd = { backStack.add(TagAddNavKey) },
            navigateToTagDetail = { backStack.add(TagDetailNavKey(TagId(it))) },
            stateHolder = koinInject(scope = scope),
        )
    }

    entry<TagAddNavKey> {
        val scope = rememberKoinScope<TagAddScope>(scopeId = TagAddScope.DEFAULT_ID, autoClose = true)

        TagAddScreen(
            navigateUp = backStack::removeLastOrNull,
            stateHolder = koinInject(scope = scope),
        )
    }

    entry<TagDetailNavKey> { navKey ->
        val scope = rememberKoinScope<TagDetailScope>(scopeId = TagDetailScope.DEFAULT_ID, autoClose = true)

        TagDetailScreen(
            navigateUp = backStack::removeLastOrNull,
            navigateToTagMemo = { backStack.add(TagMemoNavKey(navKey.tagId)) },
            stateHolder = koinInject(scope = scope) { parametersOf(navKey.tagId) },
        )
    }

    entry<TagMemoNavKey> { navKey ->
        val scope = rememberKoinScope<TagMemoScope>(scopeId = TagMemoScope.DEFAULT_ID, autoClose = true)

        TagMemoScreen(
            navigateUp = backStack::removeLastOrNull,
            navigateToMemoAdd = { backStack.add(MemoAddNavKey(tagId = navKey.tagId)) },
            navigateToMemoDetail = { backStack.add(MemoDetailNavKey(MemoId(it))) },
            stateHolder = koinInject(scope = scope) { parametersOf(navKey.tagId) },
            viewModel = koinViewModel { parametersOf(navKey.tagId) },
        )
    }
}
