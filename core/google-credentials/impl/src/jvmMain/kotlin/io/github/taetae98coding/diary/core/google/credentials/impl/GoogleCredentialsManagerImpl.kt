package io.github.taetae98coding.diary.core.google.credentials.impl

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentials
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsManager
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsNotSupportException
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsUserCancelException
import java.awt.Desktop
import java.net.URI
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

public class GoogleCredentialsManagerImpl(
    private val clientId: String,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : GoogleCredentialsManager {
    override suspend fun get(): GoogleCredentials {
        if (!Desktop.isDesktopSupported()) throw GoogleCredentialsNotSupportException()

        return withContext(dispatcher) {
            suspendCancellableCoroutine { continuation ->
                try {
                    val receiver = LocalServerReceiver.Builder()
                        .build()

                    continuation.invokeOnCancellation { receiver.stop() }

                    val redirectUri = receiver.redirectUri
                    val requestUrl = AuthorizationCodeRequestUrl(AUTHORIZATION_SERVER_URL, clientId)
                        .setScopes(listOf("openid", "email", "profile"))
                        .setRedirectUri(redirectUri)
                        .build()

                    Desktop.getDesktop().browse(URI(requestUrl))
                    continuation.resume(
                        GoogleCredentials.AuthorizationCode(
                            code = receiver.waitForCode(),
                            clientId = clientId,
                            redirectUri = redirectUri,
                        ),
                    ).also {
                        receiver.stop()
                    }
                } catch (e: NullPointerException) {
                    continuation.resumeWithException(GoogleCredentialsUserCancelException(e))
                }
            }
        }
    }

    private companion object {
        const val AUTHORIZATION_SERVER_URL = "https://accounts.google.com/o/oauth2/v2/auth"
    }
}
