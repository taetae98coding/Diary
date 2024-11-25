package io.github.taetae98coding.diary.feature.memo.tag

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.library.color.randomArgb

@DiaryPreview
@Composable
private fun LoadingMemoTagScreenPreview() {
	DiaryTheme {
		MemoTagScreen(
			navigateButtonProvider = { MemoTagNavigationButton.NavigateUp(onNavigateUp = {}) },
			onTagAdd = {},
			memoTagListProvider = { null },
			tagListProvider = { null },
		)
	}
}

@DiaryPreview
@Composable
private fun EmptyMemoTagScreenPreview() {
	DiaryTheme {
		MemoTagScreen(
			navigateButtonProvider = { MemoTagNavigationButton.NavigateUp(onNavigateUp = {}) },
			onTagAdd = {},
			memoTagListProvider = { emptyList() },
			tagListProvider = { emptyList() },
		)
	}
}

@DiaryPreview
@Composable
private fun MemoTagScreenPreview() {
	DiaryTheme {
		MemoTagScreen(
			navigateButtonProvider = { MemoTagNavigationButton.NavigateUp(onNavigateUp = {}) },
			onTagAdd = {},
			memoTagListProvider = {
				List(5) {
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
			tagListProvider = {
				List(15) {
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
		)
	}
}
