package com.taetae98.diary.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.taetae98.diary.feature.memo.MemoRoute
import com.taetae98.diary.navigation.core.app.AppEntry
import com.taetae98.diary.navigation.core.memo.MemoEntry

@Composable
internal fun AppNavHost(
    modifier: Modifier = Modifier,
    entry: AppEntry,
) {
    Children(
        modifier = modifier,
        stack = entry.stack,
    ) {
        when (val instance = it.instance) {
            is MemoEntry -> MemoRoute(entry = instance)
        }
    }
}