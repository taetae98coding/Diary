package io.github.taetae98coding.diary.app

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.app.state.rememberAppState
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@DiaryPreview
@Composable
private fun Preview() {
	DiaryTheme {
		AppNavHost(state = rememberAppState())
	}
}
