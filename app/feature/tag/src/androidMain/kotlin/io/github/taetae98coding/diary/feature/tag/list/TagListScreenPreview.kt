package io.github.taetae98coding.diary.feature.tag.list

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@DiaryPreview
@Composable
private fun LoadingPreview() {
	DiaryTheme {
		TagListScreen(
			state = rememberTagListScreenState(),
			floatingButtonProvider = { TagListFloatingButton.Add(onAdd = {}) },
			listProvider = { null },
			onTag = {},
			uiStateProvider = { TagListScreenUiState() },
		)
	}
}

@DiaryPreview
@Composable
private fun EmptyPreview() {
	DiaryTheme {
		TagListScreen(
			state = rememberTagListScreenState(),
			floatingButtonProvider = { TagListFloatingButton.Add(onAdd = {}) },
			listProvider = { emptyList() },
			onTag = {},
			uiStateProvider = { TagListScreenUiState() },
		)
	}
}

@DiaryPreview
@Composable
private fun Preview() {
	DiaryTheme {
		TagListScreen(
			state = rememberTagListScreenState(),
			floatingButtonProvider = { TagListFloatingButton.Add(onAdd = {}) },
			listProvider = {
				List(10) {
					TagListItemUiState(
						id = it.toString(),
						title = "Title $it",
						finish = SkipProperty {},
						delete = SkipProperty {},
					)
				}
			},
			onTag = {},
			uiStateProvider = { TagListScreenUiState() },
		)
	}
}
