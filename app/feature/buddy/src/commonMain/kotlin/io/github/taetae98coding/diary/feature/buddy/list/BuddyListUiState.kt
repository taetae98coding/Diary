package io.github.taetae98coding.diary.feature.buddy.list

import io.github.taetae98coding.diary.core.model.buddy.BuddyGroup

internal sealed class BuddyListUiState {
	data object Loading : BuddyListUiState()

	data class NetworkError(
		val retry: () -> Unit,
	) : BuddyListUiState()

	data class UnknownError(
		val retry: () -> Unit,
	) : BuddyListUiState()

	data class State(
		val list: List<BuddyGroup>,
	) : BuddyListUiState()
}
