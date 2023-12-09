package com.taetae98.diary.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.taetae98.diary.navigation.core.app.AppEntry
import com.taetae98.diary.navigation.core.memo.MemoEntry
import com.taetae98.diary.navigation.core.more.MoreEntry
import com.taetae98.diary.ui.compose.icon.MemoIcon
import com.taetae98.diary.ui.compose.icon.MoreIcon

@Composable
internal fun AppBottomBar(
    modifier: Modifier = Modifier,
    entry: AppEntry
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = true
    ) {
        Content(entry = entry)
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    entry: AppEntry
) {
    NavigationBar(
        modifier = modifier,
    ) {
        val child by entry.stack.subscribeAsState()
        val instance = child.active.instance

        NavigationBarItem(
            selected = instance is MemoEntry,
            onClick = entry::navigateToMemo,
            icon = { MemoIcon() },
            label = { Text(text = "메모") },
            alwaysShowLabel = false
        )

        NavigationBarItem(
            selected = instance is MoreEntry,
            onClick = entry::navigateToMore,
            icon = { MoreIcon() },
            label = { Text(text = "더보기") },
            alwaysShowLabel = false
        )
    }
}