package com.taetae98.diary.feature.more

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier

@Composable
@NonRestartableComposable
internal fun MoreRoute(
    modifier: Modifier = Modifier,
    onAccount: () -> Unit,
    onFinishedMemo: () -> Unit,
) {
    MoreScreen(
        modifier = modifier,
        onAccount = onAccount,
        onFinishedMemo = onFinishedMemo,
    )
}
