package io.github.taetae98coding.diary.core.design.system.date

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.design.system.diary.date.DiaryDate
import io.github.taetae98coding.diary.core.design.system.diary.date.rememberDiaryDateState
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import kotlinx.datetime.LocalDate

@DiaryPreview
@Composable
private fun NonePreview() {
	DiaryTheme {
		DiaryDate(state = rememberDiaryDateState(initialDateRange = null))
	}
}

@DiaryPreview
@Composable
private fun YesPreview() {
	DiaryTheme {
		DiaryDate(
			state = rememberDiaryDateState(
				initialDateRange = LocalDate(2024, 11, 1)..LocalDate(2024, 11, 1),
			),
		)
	}
}
