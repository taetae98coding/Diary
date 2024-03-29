package com.taetae98.diary.feature.finished.memo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.koin.navigation.compose.koinInject
import com.taetae98.diary.navigation.core.finished.memo.FinishedMemoEntry

@Composable
@NonRestartableComposable
public fun FinishedMemoEntryPoint(
    modifier: Modifier = Modifier,
    entry: FinishedMemoEntry
) {
    FinishedMemoRoute(
        modifier = modifier,
        onNavigateUp = entry.navigateUp,
        onMemo = entry.navigateToMemoDetail,
        viewModel = entry.koinInject(),
    )
}
