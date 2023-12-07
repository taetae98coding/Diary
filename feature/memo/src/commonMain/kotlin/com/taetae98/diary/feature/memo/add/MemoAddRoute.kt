package com.taetae98.diary.feature.memo.add

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.feature.memo.detail.MemoDetailScreen
import com.taetae98.diary.navigation.core.memo.MemoAddEntry

@Composable
internal fun MemoAddRoute(
    modifier: Modifier = Modifier,
    entry: MemoAddEntry,
) {
    MemoDetailScreen(
        modifier = modifier,
        onNavigateUp = entry.navigateUp,
    )
}