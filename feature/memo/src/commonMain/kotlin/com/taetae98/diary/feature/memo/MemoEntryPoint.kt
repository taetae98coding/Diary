package com.taetae98.diary.feature.memo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.feature.memo.add.MemoAddRoute
import com.taetae98.diary.feature.memo.list.MemoListRoute
import com.taetae98.diary.navigation.core.memo.MemoAddEntry
import com.taetae98.diary.navigation.core.memo.MemoEntry
import com.taetae98.diary.navigation.core.memo.MemoListEntry
import com.taetae98.diary.ui.decompose.compose.AnimationChildren

@Composable
public fun MemoEntryPoint(
    modifier: Modifier = Modifier,
    entry: MemoEntry,
) {
    AnimationChildren(
        modifier = modifier,
        stack = entry.stack,
    ) {
        when (val instance = it.instance) {
            is MemoListEntry -> MemoListRoute(
                onNavigateToMemoAdd = instance.navigateToMemoAdd,
            )

            is MemoAddEntry -> MemoAddRoute(
                onNavigateUp = instance.navigateUp
            )
        }
    }
}