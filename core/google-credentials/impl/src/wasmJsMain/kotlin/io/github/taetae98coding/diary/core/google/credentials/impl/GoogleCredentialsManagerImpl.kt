@file:OptIn(ExperimentalWasmJsInterop::class)

package io.github.taetae98coding.diary.core.google.credentials.impl

import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentials
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsException
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsManager
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsNotSupportException
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsUserCancelException
import kotlin.js.Promise
import kotlinx.coroutines.await

public class GoogleCredentialsManagerImpl(private val clientId: String) : GoogleCredentialsManager {
    override suspend fun get(): GoogleCredentials {
        if (!isGoogleAccountsOAuth2Available()) {
            throw GoogleCredentialsNotSupportException()
        }

        val code = try {
            requestAuthorizationCode(clientId, SCOPE).await().toString()
        } catch (cause: Throwable) {
            throw cause.toGoogleCredentialsException()
        }

        return GoogleCredentials.AuthorizationCode(
            clientId = clientId,
            code = code,
            redirectUri = REDIRECT_URI,
        )
    }

    private fun Throwable.toGoogleCredentialsException(): GoogleCredentialsException {
        val message = message.orEmpty()
        return when {
            USER_CANCEL_KEYWORDS.any { it in message } -> GoogleCredentialsUserCancelException(this)
            else -> GoogleCredentialsException(this)
        }
    }

    private companion object {
        const val SCOPE = "openid email profile"

        // GIS popup 모드에서 OAuth2 token 교환 시 사용하는 표준 redirect_uri 값.
        const val REDIRECT_URI = "postmessage"

        val USER_CANCEL_KEYWORDS = listOf(
            "popup_closed",
            "popup_failed_to_open",
            "user_cancel",
            "access_denied",
        )
    }
}

private fun isGoogleAccountsOAuth2Available(): Boolean = js(
    "typeof google !== 'undefined' && typeof google.accounts !== 'undefined' && typeof google.accounts.oauth2 !== 'undefined'",
)

private fun requestAuthorizationCode(
    clientId: String,
    scope: String,
): Promise<JsString> = js(
    """
    new Promise(function(resolve, reject) {
        var client = google.accounts.oauth2.initCodeClient({
            client_id: clientId,
            scope: scope,
            ux_mode: 'popup',
            callback: function(response) {
                if (response && response.code) {
                    resolve(response.code);
                } else {
                    reject(new Error(response && response.error ? response.error : 'unknown'));
                }
            },
            error_callback: function(err) {
                reject(new Error(err && err.type ? err.type : 'unknown'));
            }
        });
        client.requestCode();
    })
    """,
)
