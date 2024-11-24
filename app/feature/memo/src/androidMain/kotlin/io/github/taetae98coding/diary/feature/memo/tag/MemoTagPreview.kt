package io.github.taetae98coding.diary.feature.memo.tag

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.library.color.randomArgb

@DiaryPreview
@Composable
private fun PrimaryMemoTagPreview() {
	DiaryTheme {
		Surface {
			PrimaryMemoTag(
				uiState = TagUiState(
					id = "id",
					title = "Title",
					isSelected = false,
					color = randomArgb(),
					select = SkipProperty {},
					unselect = SkipProperty {},
				),
			)
		}
	}
}

@DiaryPreview
@Composable
private fun SelectedPrimaryMemoTagPreview() {
	DiaryTheme {
		Surface {
			PrimaryMemoTag(
				uiState = TagUiState(
					id = "id",
					title = "Title",
					isSelected = true,
					color = randomArgb(),
					select = SkipProperty {},
					unselect = SkipProperty {},
				),
			)
		}
	}
}

@DiaryPreview
@Composable
private fun MemoTagPreview() {
	DiaryTheme {
		Surface {
			MemoTag(
				uiState = TagUiState(
					id = "id",
					title = "Title",
					isSelected = false,
					color = randomArgb(),
					select = SkipProperty {},
					unselect = SkipProperty {},
				),
			)
		}
	}
}

@DiaryPreview
@Composable
private fun SelectedMemoTagPreview() {
	DiaryTheme {
		Surface {
			MemoTag(
				uiState = TagUiState(
					id = "id",
					title = "Title",
					isSelected = true,
					color = randomArgb(),
					select = SkipProperty {},
					unselect = SkipProperty {},
				),
			)
		}
	}
}
