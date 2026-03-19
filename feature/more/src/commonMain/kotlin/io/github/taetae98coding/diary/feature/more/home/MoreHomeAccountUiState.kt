package io.github.taetae98coding.diary.feature.more.home

internal sealed class MoreHomeAccountUiState {
    data object Loading : MoreHomeAccountUiState()
    data object NotLogin : MoreHomeAccountUiState()
    data class Login(
        val email: String,
        val profileImage: String?,
    ) : MoreHomeAccountUiState()
}
