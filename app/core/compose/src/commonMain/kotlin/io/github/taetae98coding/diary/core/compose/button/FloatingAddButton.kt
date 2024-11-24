package io.github.taetae98coding.diary.core.compose.button

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.design.system.icon.AddIcon

@Composable
public fun FloatingAddButton(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	progressProvider: () -> Boolean = { false },
) {
	FloatingActionButton(
		onClick = onClick,
		modifier = modifier,
	) {
		Crossfade(progressProvider()) {
			if (it) {
				CircularProgressIndicator(modifier = Modifier.size(24.dp))
			} else {
				AddIcon()
			}
		}
	}
}
