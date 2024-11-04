package io.github.taetae98coding.diary.core.resources.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public fun LogoutIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = Icons.AutoMirrored.Rounded.Logout,
        contentDescription = null,
        modifier = modifier,
    )
}
