package io.github.taetae98coding.diary.core.design.system.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
public fun FinishIcon(
	modifier: Modifier = Modifier,
	tint: Color = LocalContentColor.current,
) {
	Icon(
		modifier = modifier,
		imageVector = Icons.Rounded.Verified,
		contentDescription = null,
		tint = tint,
	)
}
