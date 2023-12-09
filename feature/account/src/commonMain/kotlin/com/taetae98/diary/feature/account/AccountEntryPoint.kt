package com.taetae98.diary.feature.account

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.navigation.core.account.AccountEntry

@Composable
public fun AccountEntryPoint(
    modifier: Modifier = Modifier,
    entry: AccountEntry
) {
    Text(text = "Account")
}