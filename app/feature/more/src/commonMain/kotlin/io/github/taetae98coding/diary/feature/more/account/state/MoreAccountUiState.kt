package io.github.taetae98coding.diary.feature.more.account.state

internal sealed class MoreAccountUiState {
	data object Loading : MoreAccountUiState()

	data object Guest : MoreAccountUiState()

	data class Member(val email: String, val logout: () -> Unit) : MoreAccountUiState()
}
