package com.taetae98.diary.feature.tag

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.taetae98.diary.feature.tag.add.TagAddRoute
import com.taetae98.diary.feature.tag.list.TagListRoute
import com.taetae98.diary.navigation.core.tag.TagAddEntry
import com.taetae98.diary.navigation.core.tag.TagEntry
import com.taetae98.diary.navigation.core.tag.TagListEntry

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
            )

            is TagAddEntry -> TagAddRoute(
                onNavigateUp = instance.navigateUp,
            )
        }
    }
}
