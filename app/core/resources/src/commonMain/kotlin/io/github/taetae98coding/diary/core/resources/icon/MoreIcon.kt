package io.github.taetae98coding.diary.core.resources.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public fun MoreIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = Icons.Rounded.MoreHoriz,
        contentDescription = null,
        modifier = modifier,
    )
}
