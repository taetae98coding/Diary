package io.github.taetae98coding.diary.feature.login.google

import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.retain

@Composable
internal actual fun rememberGoogleLoginState(onLogin: () -> Unit): GoogleLoginState {
    return retain {
        object : GoogleLoginState {
            override fun login() {
                onLogin()
            }
        }
    }
}
