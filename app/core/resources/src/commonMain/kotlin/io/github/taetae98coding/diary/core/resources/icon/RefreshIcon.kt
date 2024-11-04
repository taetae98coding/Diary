package io.github.taetae98coding.diary.core.resources.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public fun RefreshIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = Icons.Rounded.Refresh,
        contentDescription = null,
        modifier = modifier,
    )
}
