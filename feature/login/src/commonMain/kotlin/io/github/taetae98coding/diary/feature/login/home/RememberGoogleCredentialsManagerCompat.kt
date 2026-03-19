package io.github.taetae98coding.diary.feature.login.home

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsManager

@Composable
internal expect fun rememberGoogleCredentialsManagerCompat(): GoogleCredentialsManager
