package io.github.taetae98coding.diary.feature.account.join.state

internal data class JoinUiState(
    val isProgress: Boolean = false,
    val isLoginFinish: Boolean = false,
    val isExistEmail: Boolean = false,
    val isNetworkError: Boolean = false,
    val isUnknownError: Boolean = false,
    val onMessageShow: () -> Unit = {},
) {
    val hasMessage = isLoginFinish || isExistEmail || isNetworkError || isUnknownError
}
