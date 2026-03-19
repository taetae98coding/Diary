package io.github.taetae98coding.diary.feature.login.home

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsManager
import io.github.taetae98coding.diary.core.google.credentials.compose.rememberGoogleCredentialsManager

@Composable
internal actual fun rememberGoogleCredentialsManagerCompat(): GoogleCredentialsManager {
    return rememberGoogleCredentialsManager()
}
