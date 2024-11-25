package io.github.taetae98coding.diary.feature.tag.memo

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import kotlinx.datetime.LocalDate

@DiaryPreview
@Composable
private fun LoadingPreview() {
	DiaryTheme {
		TagMemoScreen(
			state = rememberTagMemoScreenState(),
			navigateButtonProvider = { TagMemoNavigateButton.NavigateUp(onNavigateUp = {}) },
			uiStateProvider = { TagMemoScreenUiState() },
			onAdd = {},
			listProvider = { null },
			onMemo = {},
		)
	}
}

@DiaryPreview
@Composable
private fun EmptyPreview() {
	DiaryTheme {
		TagMemoScreen(
			state = rememberTagMemoScreenState(),
			navigateButtonProvider = { TagMemoNavigateButton.NavigateUp(onNavigateUp = {}) },
			uiStateProvider = { TagMemoScreenUiState() },
			onAdd = {},
			listProvider = { emptyList() },
			onMemo = {},
		)
	}
}

@DiaryPreview
@Composable
private fun Preview() {
	DiaryTheme {
		TagMemoScreen(
			state = rememberTagMemoScreenState(),
			navigateButtonProvider = { TagMemoNavigateButton.NavigateUp(onNavigateUp = {}) },
			uiStateProvider = { TagMemoScreenUiState() },
			onAdd = {},
			listProvider = {
				List(10) {
					MemoListItemUiState(
						id = it.toString(),
						title = "Title $it",
						dateRange = if (it % 3 == 0) {
							null
						} else if (it % 3 == 1) {
							LocalDate(2000, 1, 1)..LocalDate(2000, 1, 1)
						} else {
							LocalDate(2000, 1, 1)..LocalDate(2000, 1, 2)
						},
						finish = SkipProperty {},
						delete = SkipProperty {},
					)
				}
			},
			onMemo = {},
		)
	}
}
