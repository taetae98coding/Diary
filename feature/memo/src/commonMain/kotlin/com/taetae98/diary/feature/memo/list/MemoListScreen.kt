package com.taetae98.diary.feature.memo.list

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.ui.compose.button.AddFloatingButton

@Composable
internal fun MemoListScreen(
    modifier: Modifier = Modifier,
    onAdd: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = { AddFloatingButton(onAdd = onAdd) }
    ) {

    }
}
