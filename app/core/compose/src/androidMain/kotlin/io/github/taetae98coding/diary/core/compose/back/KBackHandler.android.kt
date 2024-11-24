package io.github.taetae98coding.diary.core.compose.back

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
public actual fun KBackHandler(
	isEnabled: Boolean,
	onBack: () -> Unit,
) {
	BackHandler(
		enabled = isEnabled,
		onBack = onBack,
	)
}
