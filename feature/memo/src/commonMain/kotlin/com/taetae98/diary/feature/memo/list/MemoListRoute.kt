package com.taetae98.diary.feature.memo.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun MemoListRoute(
    modifier: Modifier = Modifier,
    onNavigateToMemoAdd: () -> Unit,
) {
    MemoListScreen(
        modifier = modifier,
        onAdd = onNavigateToMemoAdd,
    )
}