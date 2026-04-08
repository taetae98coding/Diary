package io.github.taetae98coding.diary.feature.memo

import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.DialogSceneStrategy
import io.github.taetae98coding.diary.compose.core.coroutine.retainCoroutineScope
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
import io.github.taetae98coding.diary.feature.memo.add.MemoAddStateHolder
import io.github.taetae98coding.diary.feature.memo.add.MemoAddTagScreen
import io.github.taetae98coding.diary.feature.memo.detail.MemoDetailScreen
import io.github.taetae98coding.diary.feature.memo.detail.MemoDetailTagScreen
import io.github.taetae98coding.diary.feature.memo.di.MemoAddScope
import io.github.taetae98coding.diary.feature.memo.home.AccountMemoListFilterStrategy
import io.github.taetae98coding.diary.feature.memo.home.AccountMemoListStrategy
import io.github.taetae98coding.diary.feature.memo.home.MemoHomeScreen
import io.github.taetae98coding.diary.feature.memo.home.MemoListFilterDialog
import io.github.taetae98coding.diary.library.koin.compose.rememberCoroutineKoinScope
import io.github.taetae98coding.diary.presenter.memo.api.ListMemoFilterStateHolder
import io.github.taetae98coding.diary.presenter.memo.api.MemoListStateHolder
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

public fun EntryProviderScope<NavKey>.memoEntry(backStack: NavBackStack<NavKey>) {
    entry<MemoHomeNavKey> {
        val koin = getKoin()
        val coroutineScope = retainCoroutineScope()
        val memoListStateHolder = retain(coroutineScope) {
            val strategy by koin.inject<AccountMemoListStrategy>()

            MemoListStateHolder(
                coroutineScope = coroutineScope,
                strategy = strategy,
            )
        }
        val filterStateHolder = retain(coroutineScope) {
            val strategy by koin.inject<AccountMemoListFilterStrategy>()

            ListMemoFilterStateHolder(
                coroutineScope = coroutineScope,
                strategy = strategy,
            )
        }

        MemoHomeScreen(
            navigateToMemoAdd = { backStack.add(MemoAddNavKey()) },
            navigateToMemoDetail = { backStack.add(MemoDetailNavKey(MemoId(it))) },
            navigateToFilter = { backStack.add(MemoListFilterNavKey) },
            memoListStateHolder = memoListStateHolder,
            filterStateHolder = filterStateHolder,
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
        val scope = rememberCoroutineKoinScope<MemoAddScope>(MemoAddScope.DEFAULT_ID)
        val stateHolder = remember { scope.inject<MemoAddStateHolder> { parametersOf(navKey.tagId) }.value }

        MemoAddScreen(
            navKey = navKey,
            navigateUp = backStack::removeLastOrNull,
            navigateToMemoAddTag = { backStack.add(MemoAddTagNavKey) },
            navigateToTagDetail = { backStack.add(TagDetailNavKey(TagId(it))) },
            stateHolder = stateHolder,
        )
    }

    entry<MemoAddTagNavKey> {
        val koin = getKoin()
        val stateHolder = remember { koin.getScope(scopeId = MemoAddScope.DEFAULT_ID).inject<MemoAddStateHolder>().value }

        MemoAddTagScreen(
            navigateUp = backStack::removeLastOrNull,
            navigateToTagAdd = { backStack.add(TagAddNavKey) },
            stateHolder = stateHolder,
        )
    }

    entry<MemoDetailNavKey> { navKey ->
        MemoDetailScreen(
            navigateUp = backStack::removeLastOrNull,
            navigateToMemoDetailTag = { backStack.add(MemoDetailTagNavKey(navKey.memoId)) },
            navigateToTagDetail = { backStack.add(TagDetailNavKey(TagId(it))) },
            viewModel = koinViewModel { parametersOf(navKey.memoId) },
        )
    }

    entry<MemoDetailTagNavKey> { navKey ->
        MemoDetailTagScreen(
            navigateUp = backStack::removeLastOrNull,
            navigateToTagAdd = { backStack.add(TagAddNavKey) },
            viewModel = koinViewModel { parametersOf(navKey.memoId) },
        )
    }
}
