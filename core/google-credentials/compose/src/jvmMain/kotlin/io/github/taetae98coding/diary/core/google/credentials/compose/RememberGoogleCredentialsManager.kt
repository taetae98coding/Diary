package io.github.taetae98coding.diary.core.google.credentials.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsManager
import io.github.taetae98coding.diary.core.google.credentials.impl.GoogleCredentialsManagerImpl

@Composable
public fun rememberGoogleCredentialsManager(clientId: String): GoogleCredentialsManager {
    return remember(clientId) { GoogleCredentialsManagerImpl(clientId) }
}
