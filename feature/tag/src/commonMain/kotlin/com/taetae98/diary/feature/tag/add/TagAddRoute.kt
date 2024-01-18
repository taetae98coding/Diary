package com.taetae98.diary.feature.tag.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.taetae98.diary.ui.compose.text.TextFieldUiState

@Composable
internal fun TagAddRoute(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
) {
    TagAddScreen(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        titleUiState = remember { mutableStateOf(TextFieldUiState("", {})) },
        onAdd = {},
    )
}
