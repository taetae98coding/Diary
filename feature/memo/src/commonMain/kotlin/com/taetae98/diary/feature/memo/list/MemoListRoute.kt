package com.taetae98.diary.feature.memo.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.navigation.core.memo.MemoListEntry

@Composable
internal fun MemoListRoute(
    modifier: Modifier = Modifier,
    entry: MemoListEntry,
) {
    MemoListScreen(
        modifier = modifier,
        onAdd = entry.navigateToMemoAdd,
    )
}