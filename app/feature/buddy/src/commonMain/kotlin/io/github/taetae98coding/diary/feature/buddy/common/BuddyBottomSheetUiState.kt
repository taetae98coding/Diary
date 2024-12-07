package io.github.taetae98coding.diary.feature.buddy.common

internal sealed class BuddyBottomSheetUiState {
	data object Loading : BuddyBottomSheetUiState()

	data object NetworkError : BuddyBottomSheetUiState()

	data class State(
		val buddyList: List<BuddyUiState>,
	) : BuddyBottomSheetUiState()
}
