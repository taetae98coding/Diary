package com.taetae98.diary.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.feature.account.AccountEntryPoint
import com.taetae98.diary.feature.memo.MemoEntryPoint
import com.taetae98.diary.feature.more.MoreEntryPoint
import com.taetae98.diary.navigation.core.account.AccountEntry
import com.taetae98.diary.navigation.core.app.AppEntry
import com.taetae98.diary.navigation.core.memo.MemoEntry
import com.taetae98.diary.navigation.core.more.MoreEntry
import com.taetae98.diary.ui.decompose.compose.AnimationChildren

@Composable
internal fun AppNavHost(
    modifier: Modifier = Modifier,
    entry: AppEntry,
) {
    AnimationChildren(
        modifier = modifier,
        stack = entry.stack,
    ) {
        when (val instance = it.instance) {
            is MemoEntry -> MemoEntryPoint(entry = instance)
            is MoreEntry -> MoreEntryPoint(entry = instance)
            is AccountEntry -> AccountEntryPoint(entry = instance)
        }
    }
}