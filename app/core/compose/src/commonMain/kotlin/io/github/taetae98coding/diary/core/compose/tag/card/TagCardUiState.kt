package io.github.taetae98coding.diary.core.compose.tag.card

public sealed class TagCardUiState {
	public data object Loading : TagCardUiState()

	public data class State(
		val list: List<TagCardItemUiState>,
	) : TagCardUiState()

	public data class NetworkError(
		val refresh: () -> Unit,
	) : TagCardUiState()

	public data class UnknownError(
		val refresh: () -> Unit,
	) : TagCardUiState()
}
