package com.taetae98.diary.ui.compose.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
public fun TagIcon(
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    Icon(
        modifier = modifier,
        imageVector = Icons.Rounded.Tag,
        contentDescription = null,
        tint = tint,
    )
}