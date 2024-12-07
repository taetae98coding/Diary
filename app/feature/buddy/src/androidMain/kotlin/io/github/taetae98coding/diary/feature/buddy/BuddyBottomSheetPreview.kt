package io.github.taetae98coding.diary.feature.buddy

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.feature.buddy.common.BuddyBottomSheet
import io.github.taetae98coding.diary.feature.buddy.common.BuddyBottomSheetUiState
import io.github.taetae98coding.diary.feature.buddy.common.rememberBuddyBottomSheetState

@Composable
@DiaryPreview
private fun EmptyPreview() {
	DiaryTheme {
		BuddyBottomSheet(
			state = rememberBuddyBottomSheetState(),
			onDismissRequest = {},
			uiStateProvider = {
				BuddyBottomSheetUiState.State(buddyList = emptyList())
			},
		)
	}
}

@Composable
@DiaryPreview
private fun Preview() {
	DiaryTheme {
		BuddyBottomSheet(
			state = rememberBuddyBottomSheetState(),
			onDismissRequest = {},
			uiStateProvider = {
				BuddyBottomSheetUiState.Loading
			},
		)
	}
}
