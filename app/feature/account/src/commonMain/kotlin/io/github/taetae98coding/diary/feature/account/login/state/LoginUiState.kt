package io.github.taetae98coding.diary.feature.account.login.state

internal data class LoginUiState(
	val isProgress: Boolean = false,
	val isLoginFinish: Boolean = false,
	val isAccountNotFound: Boolean = false,
	val isNetworkError: Boolean = false,
	val isUnknownError: Boolean = false,
	val onMessageShow: () -> Unit = {},
) {
	val hasMessage = isLoginFinish || isAccountNotFound || isNetworkError || isUnknownError
}
