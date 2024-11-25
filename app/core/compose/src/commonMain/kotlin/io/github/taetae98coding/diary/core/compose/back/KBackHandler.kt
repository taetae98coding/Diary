package io.github.taetae98coding.diary.core.compose.back

import androidx.compose.runtime.Composable

@Composable
public expect fun KBackHandler(
	isEnabled: Boolean = true,
	onBack: () -> Unit,
)
