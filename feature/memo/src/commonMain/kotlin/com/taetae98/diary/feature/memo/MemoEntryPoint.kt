package com.taetae98.diary.feature.memo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.slot.ChildSlot
import com.taetae98.diary.feature.memo.add.MemoAddRoute
import com.taetae98.diary.feature.memo.detail.MemoDetailRoute
import com.taetae98.diary.feature.memo.list.MemoListRoute
import com.taetae98.diary.feature.memo.list.TagDialogRoute
import com.taetae98.diary.library.koin.navigation.compose.koinInject
import com.taetae98.diary.navigation.core.memo.MemoAddEntry
import com.taetae98.diary.navigation.core.memo.MemoDetailEntry
import com.taetae98.diary.navigation.core.memo.MemoEntry
import com.taetae98.diary.navigation.core.memo.MemoListEntry
import com.taetae98.diary.navigation.core.memo.MemoListTagDialogEntry

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
                onNavigateToMemoTag = instance.navigateToTagDialog,
                viewModel = instance.koinInject(),
                tagViewModel = instance.koinInject(),
                onNavigateToMemoDetail = instance.navigateToMemoDetail,
            )

            is MemoAddEntry -> MemoAddRoute(
                onNavigateUp = instance.navigateUp,
                viewModel = instance.koinInject(instance.savedState),
            )

            is MemoDetailEntry -> MemoDetailRoute(
                onNavigateUp = instance.navigateUp,
                memoDetailViewModel = instance.koinInject(instance.savedState),
                memoDetailTagViewModel = instance.koinInject(instance.savedState),
            )
        }
    }

    Slot(
        state = entry.slot.subscribeAsState()
    )
}

@Composable
private fun Slot(
    state: State<ChildSlot<*, ComponentContext>>
) {
    when (val instance = state.value.child?.instance) {
        is MemoListTagDialogEntry -> TagDialogRoute(
            onDismiss = instance.onDismiss,
            tagViewModel = instance.koinInject()
        )
    }
}