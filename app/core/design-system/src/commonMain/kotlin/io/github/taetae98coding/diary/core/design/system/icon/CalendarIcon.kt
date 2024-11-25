package io.github.taetae98coding.diary.core.design.system.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public fun CalendarIcon(
	modifier: Modifier = Modifier,
) {
	Icon(
		imageVector = Icons.Rounded.CalendarToday,
		contentDescription = null,
		modifier = modifier,
	)
}
