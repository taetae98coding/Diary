package io.github.taetae98coding.diary.feature.login.google

import androidx.compose.runtime.Composable

internal interface GoogleLoginState {
    fun login()
}

@Composable
internal expect fun rememberGoogleLoginState(onLogin: () -> Unit): GoogleLoginState
