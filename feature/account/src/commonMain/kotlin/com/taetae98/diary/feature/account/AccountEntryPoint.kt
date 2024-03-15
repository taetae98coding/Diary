package com.taetae98.diary.feature.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.koin.navigation.compose.koinInject
import com.taetae98.diary.navigation.core.account.AccountEntry

@Composable
@NonRestartableComposable
public fun AccountEntryPoint(
    modifier: Modifier = Modifier,
    entry: AccountEntry
) {
    AccountRoute(
        modifier = modifier,
        onNavigateUp = entry.navigateUp,
        viewModel = entry.koinInject()
    )
}