package com.taetae98.diary.feature.more

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.taetae98.diary.navigation.core.more.MoreEntry

@Composable
@NonRestartableComposable
public fun MoreEntryPoint(
    modifier: Modifier = Modifier,
    entry: MoreEntry
) {
    MoreRoute(
        modifier = modifier,
        onAccount = entry.navigateToAccount,
        onFinishedMemo = entry.navigateToFinishedMemo
    )
}