package io.github.taetae98coding.diary.core.resources.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public fun DeleteIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        modifier = modifier,
        imageVector = Icons.Rounded.Delete,
        contentDescription = null,
    )
}
