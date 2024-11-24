package io.github.taetae98coding.diary.feature.memo.detail

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.feature.memo.add.rememberMemoDetailScreenAddState
import io.github.taetae98coding.diary.library.color.randomArgb
import kotlinx.datetime.LocalDate

@DiaryPreview
@Composable
private fun AddPreview() {
	DiaryTheme {
		MemoDetailScreen(
			state = rememberMemoDetailScreenAddState(
				initialStart = null,
				initialEndInclusive = null,
			),
			titleProvider = { "Title" },
			navigateButtonProvider = { MemoDetailNavigationButton.NavigateUp(onNavigateUp = {}) },
			actionButtonProvider = { MemoDetailActionButton.None },
			floatingButtonProvider = { MemoDetailFloatingButton.Add(onAdd = {}) },
			uiStateProvider = { MemoDetailScreenUiState() },
			onTagTitle = {},
			onTag = {},
			tagListProvider = { null },
		)
	}
}

@DiaryPreview
@Composable
private fun DetailPreview() {
	DiaryTheme {
		MemoDetailScreen(
			state = rememberMemoDetailScreenDetailState(
				onDelete = {},
				onUpdate = {},
				detailProvider = {
					MemoDetail(
						title = "Title",
						description = "Description",
						start = LocalDate(2024, 11, 1),
						endInclusive = LocalDate(2024, 11, 1),
						color = randomArgb(),
					)
				},
			),
			titleProvider = { "Title" },
			navigateButtonProvider = { MemoDetailNavigationButton.NavigateUp(onNavigateUp = {}) },
			actionButtonProvider = {
				MemoDetailActionButton.FinishAndDetail(
					isFinish = false,
					onFinishChange = {},
					delete = {},
				)
			},
			floatingButtonProvider = { MemoDetailFloatingButton.None },
			uiStateProvider = { MemoDetailScreenUiState() },
			onTagTitle = {},
			onTag = {},
			tagListProvider = { null },
		)
	}
}
