package com.taetae98.diary.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.feature.memo.MemoRoute
import com.taetae98.diary.feature.more.MoreRoute
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
            is MemoEntry -> MemoRoute(entry = instance)
            is MoreEntry -> MoreRoute(entry = instance)
        }
    }
}