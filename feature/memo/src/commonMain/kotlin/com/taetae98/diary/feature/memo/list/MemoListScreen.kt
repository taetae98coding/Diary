package com.taetae98.diary.feature.memo.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun MemoListScreen(
    modifier: Modifier = Modifier,
    onAdd: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = { AddButton(onAdd = onAdd) }
    ) {

    }
}

@Composable
private fun AddButton(
    modifier: Modifier = Modifier,
    onAdd: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onAdd,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
        )
    }
}