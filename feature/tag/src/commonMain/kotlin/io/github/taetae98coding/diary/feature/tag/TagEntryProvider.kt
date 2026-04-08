package io.github.taetae98coding.diary.feature.tag

import androidx.compose.runtime.retain.retain
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.taetae98coding.diary.compose.core.coroutine.retainCoroutineScope
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
import io.github.taetae98coding.diary.feature.tag.home.TagHomeScreen
import io.github.taetae98coding.diary.feature.tag.memo.AccountTagMemoListStrategy
import io.github.taetae98coding.diary.feature.tag.memo.TagMemoScreen
import io.github.taetae98coding.diary.presenter.memo.api.MemoListStateHolder
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

public fun EntryProviderScope<NavKey>.tagEntry(backStack: NavBackStack<NavKey>) {
    entry<TagHomeNavKey> {
        TagHomeScreen(
            navigateToTagAdd = { backStack.add(TagAddNavKey) },
            navigateToTagDetail = { backStack.add(TagDetailNavKey(TagId(it))) },
            navigateToTagMemo = { backStack.add(TagMemoNavKey(TagId(it))) },
        )
    }

    entry<TagAddNavKey> {
        TagAddScreen(
            navigateUp = backStack::removeLastOrNull,
        )
    }

    entry<TagDetailNavKey> { navKey ->
        TagDetailScreen(
            navigateUp = backStack::removeLastOrNull,
            navigateToTagMemo = { backStack.add(TagMemoNavKey(navKey.tagId)) },
            viewModel = koinViewModel { parametersOf(navKey.tagId) },
        )
    }

    entry<TagMemoNavKey> { navKey ->
        val koin = getKoin()
        val coroutineScope = retainCoroutineScope()
        val stateHolder = retain(coroutineScope) {
            val strategy by koin.inject<AccountTagMemoListStrategy> { parametersOf(navKey.tagId) }

            MemoListStateHolder(
                coroutineScope = coroutineScope,
                strategy = strategy,
            )
        }

        TagMemoScreen(
            navigateUp = backStack::removeLastOrNull,
            navigateToMemoAdd = { backStack.add(MemoAddNavKey(tagId = navKey.tagId)) },
            navigateToMemoDetail = { backStack.add(MemoDetailNavKey(MemoId(it))) },
            stateHolder = stateHolder,
            viewModel = koinViewModel { parametersOf(navKey.tagId) },
        )
    }
}
