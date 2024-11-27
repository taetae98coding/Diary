package io.github.taetae98coding.diary.feature.tag.detail

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.feature.tag.add.rememberTagDetailScreenAddState
import io.github.taetae98coding.diary.library.color.randomArgb

@DiaryPreview
@Composable
private fun AddPreview() {
	DiaryTheme {
		TagDetailScreen(
			state = rememberTagDetailScreenAddState(),
			titleProvider = { "Title" },
			navigateButtonProvider = { TagDetailNavigationButton.NavigateUp(onNavigateUp = {}) },
			actionButtonProvider = { TagDetailActionButton.None },
			floatingButtonProvider = { TagDetailFloatingButton.Add(onAdd = {}) },
			uiStateProvider = { TagDetailScreenUiState() },
		)
	}
}

@DiaryPreview
@Composable
private fun DetailPreview() {
	DiaryTheme {
		TagDetailScreen(
			state = rememberTagDetailScreenDetailState(
				onUpdate = {},
				onDelete = {},
				onMemo = {},
				detailProvider = {
					TagDetail(
						title = "Title",
						description = "Description",
						color = randomArgb(),
					)
				},
			),
			titleProvider = { "Title" },
			navigateButtonProvider = { TagDetailNavigationButton.NavigateUp(onNavigateUp = {}) },
			actionButtonProvider = {
				TagDetailActionButton.FinishAndDetail(
					isFinish = true,
					onFinishChange = {},
					delete = {},
				)
			},
			floatingButtonProvider = { TagDetailFloatingButton.None },
			uiStateProvider = { TagDetailScreenUiState() },
		)
	}
}
