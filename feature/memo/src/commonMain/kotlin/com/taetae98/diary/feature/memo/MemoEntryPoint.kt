package com.taetae98.diary.feature.memo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.taetae98.diary.feature.memo.add.MemoAddRoute
import com.taetae98.diary.feature.memo.detail.MemoDetailRoute
import com.taetae98.diary.feature.memo.list.MemoListRoute
import com.taetae98.diary.library.koin.navigation.compose.koinInject
import com.taetae98.diary.navigation.core.memo.MemoAddEntry
import com.taetae98.diary.navigation.core.memo.MemoDetailEntry
import com.taetae98.diary.navigation.core.memo.MemoEntry
import com.taetae98.diary.navigation.core.memo.MemoListEntry

@Composable
public fun MemoEntryPoint(
    modifier: Modifier = Modifier,
    entry: MemoEntry,
) {
    Children(
        modifier = modifier,
        stack = entry.stack,
    ) {
        when (val instance = it.instance) {
            is MemoListEntry -> MemoListRoute(
                onNavigateToMemoAdd = instance.navigateToMemoAdd,
                viewModel = instance.koinInject(),
                onNavigateToMemoDetail = instance.navigateToMemoDetail,
            )

            is MemoAddEntry -> MemoAddRoute(
                onNavigateUp = instance.navigateUp,
                viewModel = instance.koinInject(),
            )

            is MemoDetailEntry -> MemoDetailRoute(
                onNavigateUp = instance.navigateUp,
                memoDetailViewModel = instance.koinInject(instance.savedState),
            )
        }
    }
}
