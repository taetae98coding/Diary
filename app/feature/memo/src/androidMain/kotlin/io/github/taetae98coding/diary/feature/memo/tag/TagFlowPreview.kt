package io.github.taetae98coding.diary.feature.memo.tag

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.library.color.randomArgb

@DiaryPreview
@Composable
private fun LoadingTagFlowPreview() {
	DiaryTheme {
		Surface {
			TagFlow(
				title = { Text(text = "Title") },
				listProvider = { null },
				empty = { Text(text = "Empty") },
				tag = { MemoTag(uiState = it) },
			)
		}
	}
}

@DiaryPreview
@Composable
private fun EmptyTagFlowPreview() {
	DiaryTheme {
		Surface {
			TagFlow(
				title = { Text(text = "Title") },
				listProvider = { emptyList() },
				empty = { Text(text = "Empty") },
				tag = { MemoTag(uiState = it) },
			)
		}
	}
}

@DiaryPreview
@Composable
private fun TagFlowPreview() {
	DiaryTheme {
		Surface {
			TagFlow(
				title = { Text(text = "Title") },
				listProvider = {
					List(10) {
						TagUiState(
							id = it.toString(),
							title = "Title $it",
							isSelected = it == 0,
							color = randomArgb(),
							select = SkipProperty {},
							unselect = SkipProperty {},
						)
					}
				},
				empty = { Text(text = "Empty") },
				tag = { MemoTag(uiState = it) },
			)
		}
	}
}
