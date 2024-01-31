package com.taetae98.diary.feature.tag

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.taetae98.diary.feature.tag.add.TagAddRoute
import com.taetae98.diary.feature.tag.list.TagListRoute
import com.taetae98.diary.feature.tag.memo.TagMemoRoute
import com.taetae98.diary.library.koin.navigation.compose.koinInject
import com.taetae98.diary.navigation.core.tag.TagAddEntry
import com.taetae98.diary.navigation.core.tag.TagEntry
import com.taetae98.diary.navigation.core.tag.TagListEntry
import com.taetae98.diary.navigation.core.tag.TagMemoEntry

@Composable
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

            is TagMemoEntry -> TagMemoRoute()
        }
    }
}
