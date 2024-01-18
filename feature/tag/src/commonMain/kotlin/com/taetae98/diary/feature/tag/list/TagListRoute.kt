package com.taetae98.diary.feature.tag.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun TagListRoute(
    modifier: Modifier = Modifier,
    onNavigateToTagAdd: () -> Unit,
) {
    TagListScreen(
        modifier = modifier,
        onAdd = onNavigateToTagAdd,
    )
}