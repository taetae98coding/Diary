package io.github.taetae98coding.diary.core.design.system.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
public fun FilterIcon(
	modifier: Modifier = Modifier,
	tint: Color = LocalContentColor.current,
) {
	Icon(
		imageVector = Icons.Rounded.FilterList,
		contentDescription = null,
		modifier = modifier,
		tint = tint,
	)
}
