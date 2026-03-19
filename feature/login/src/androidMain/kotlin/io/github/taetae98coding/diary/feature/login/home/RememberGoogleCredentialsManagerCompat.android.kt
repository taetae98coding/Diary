package io.github.taetae98coding.diary.feature.login.home

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsManager
import io.github.taetae98coding.diary.core.google.credentials.compose.rememberGoogleCredentialsManager
import io.github.taetae98coding.diary.feature.login.di.GoogleClientId
import org.koin.compose.koinInject
import org.koin.core.qualifier.StringQualifier

@Composable
internal actual fun rememberGoogleCredentialsManagerCompat(): GoogleCredentialsManager {
    return rememberGoogleCredentialsManager(clientId = koinInject(qualifier = StringQualifier(requireNotNull(GoogleClientId::class.simpleName))))
}
