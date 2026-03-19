package io.github.taetae98coding.diary.core.google.credentials.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.uikit.LocalUIViewController
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsManager
import io.github.taetae98coding.diary.core.google.credentials.impl.GoogleCredentialsManagerImpl

@Composable
public fun rememberGoogleCredentialsManager(): GoogleCredentialsManager {
    val uiViewController = LocalUIViewController.current

    return remember(uiViewController) {
        GoogleCredentialsManagerImpl(uiViewController)
    }
}
