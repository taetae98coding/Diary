package com.taetae98.diary.feature.tag

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.taetae98.diary.feature.tag.add.TagAddRoute
import com.taetae98.diary.feature.tag.detail.TagDetailRoute
import com.taetae98.diary.feature.tag.list.TagListRoute
import com.taetae98.diary.feature.tag.memo.TagMemoRoute
import com.taetae98.diary.library.koin.navigation.compose.koinInject
import com.taetae98.diary.navigation.core.tag.TagAddEntry
import com.taetae98.diary.navigation.core.tag.TagDetailEntry
import com.taetae98.diary.navigation.core.tag.TagEntry
import com.taetae98.diary.navigation.core.tag.TagListEntry
import com.taetae98.diary.navigation.core.tag.TagMemoEntry

@Composable
@NonRestartableComposable
public fun TagEntryPoint(
    modifier: Modifier = Modifier,
    entry: TagEntry,
) {
    Children(
        modifier = modifier,
        stack = entry.stack,
    ) {
        when (val instance = it.instance) {
            is TagListEntry -> TagListRoute(
                onNavigateToTagAdd = instance.navigateToTagAdd,
                onNavigateToTagMemo = instance.navigateToTagMemo,
                viewModel = instance.koinInject(),
            )

            is TagAddEntry -> TagAddRoute(
                onNavigateUp = instance.navigateUp,
                viewModel = instance.koinInject(),
            )

            is TagMemoEntry -> TagMemoRoute(
                onNavigateUp = instance.navigateUp,
                onNavigateToTagDetail = instance::onNavigateToTagDetail,
                onNavigateToMemoAdd = instance::onNavigateToMemoAdd,
                onNavigateToMemoDetail = instance.navigateToMemoDetail,
                tagMemoViewModel = instance.koinInject(instance.savedState),
                tagMemoPagingViewModel = instance.koinInject(instance.savedState),
            )

            is TagDetailEntry -> TagDetailRoute(
                onNavigateUp = instance.navigateUp,
                viewModel = instance.koinInject(instance.savedState),
                toolbarViewModel = instance.koinInject(instance.savedState)
            )
        }
    }
}
