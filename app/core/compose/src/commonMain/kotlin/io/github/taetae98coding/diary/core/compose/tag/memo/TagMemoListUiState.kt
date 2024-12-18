package io.github.taetae98coding.diary.core.compose.tag.memo

import io.github.taetae98coding.diary.core.compose.memo.list.MemoListItemUiState

public sealed class TagMemoListUiState {
	public data object Loading : TagMemoListUiState()

	public data class State(
		val list: List<MemoListItemUiState>,
	) : TagMemoListUiState()

	public data class NetworkError(
		val retry: () -> Unit,
	) : TagMemoListUiState()

	public data class UnknownError(
		val retry: () -> Unit,
	) : TagMemoListUiState()
}
