package com.taetae98.diary.feature.memo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.taetae98.diary.feature.memo.add.MemoAddRoute
import com.taetae98.diary.feature.memo.list.MemoListRoute
import com.taetae98.diary.navigation.core.memo.MemoAddEntry
import com.taetae98.diary.navigation.core.memo.MemoEntry
import com.taetae98.diary.navigation.core.memo.MemoListEntry

@Composable
public fun MemoRoute(
    modifier: Modifier = Modifier,
    entry: MemoEntry,
) {
    Children(
        modifier = modifier,
        stack = entry.stack,
    ) {
        when (val instance = it.instance) {
            is MemoListEntry -> MemoListRoute(entry = instance)
            is MemoAddEntry ->  MemoAddRoute(entry = instance)
        }
    }
}