package com.taetae98.diary.feature.memo.add

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.feature.memo.detail.MemoDetailScreen

@Composable
internal fun MemoAddRoute(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
) {
    MemoDetailScreen(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
    )
}