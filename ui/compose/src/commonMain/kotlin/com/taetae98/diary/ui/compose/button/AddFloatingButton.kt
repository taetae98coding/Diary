package com.taetae98.diary.ui.compose.button

import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.ui.compose.icon.AddIcon

@Composable
public fun AddFloatingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick
    ) {
        AddIcon()
    }
}