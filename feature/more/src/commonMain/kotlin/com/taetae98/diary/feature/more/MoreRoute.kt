package com.taetae98.diary.feature.more

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun MoreRoute(
    modifier: Modifier = Modifier,
    onAccount: () -> Unit
) {
    MoreScreen(
        modifier = modifier,
        onAccount = onAccount,
    )
}