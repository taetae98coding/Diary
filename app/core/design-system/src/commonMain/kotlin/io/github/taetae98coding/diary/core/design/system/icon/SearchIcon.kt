package io.github.taetae98coding.diary.core.design.system.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public fun SearchIcon(
	modifier: Modifier = Modifier,
) {
	Icon(
		imageVector = Icons.Rounded.Search,
		contentDescription = null,
		modifier = modifier,
	)
}
