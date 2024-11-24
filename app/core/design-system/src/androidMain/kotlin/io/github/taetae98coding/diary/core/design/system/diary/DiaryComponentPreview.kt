package io.github.taetae98coding.diary.core.design.system.diary

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.design.system.diary.component.DiaryComponent
import io.github.taetae98coding.diary.core.design.system.diary.component.rememberDiaryComponentState
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@Composable
@DiaryPreview
private fun PreviewDiaryComponent() {
	DiaryTheme {
		DiaryComponent(
			state = rememberDiaryComponentState(),
		)
	}
}
