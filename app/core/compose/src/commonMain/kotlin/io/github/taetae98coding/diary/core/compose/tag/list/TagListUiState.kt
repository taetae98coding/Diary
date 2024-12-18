package io.github.taetae98coding.diary.core.compose.tag.list

public sealed class TagListUiState {
	public data object Loading : TagListUiState()

	public data class NetworkError(
		val retry: () -> Unit,
	) : TagListUiState()

	public data class UnknownError(
		val retry: () -> Unit,
	) : TagListUiState()

	public data class State(
		public val list: List<TagListItemUiState>,
	) : TagListUiState()
}
